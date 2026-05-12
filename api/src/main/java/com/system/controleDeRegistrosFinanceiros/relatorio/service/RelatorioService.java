package com.system.controleDeRegistrosFinanceiros.relatorio.service;
import com.system.controleDeRegistrosFinanceiros.authentication.model.User;
import com.system.controleDeRegistrosFinanceiros.cobranca.model.dto.CobrancaQueryFilters;
import com.system.controleDeRegistrosFinanceiros.exceptions.ResourceNotFoundException;
import com.system.controleDeRegistrosFinanceiros.relatorio.mapper.DadosRelatorioMapper;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.system.controleDeRegistrosFinanceiros.cobranca.model.entity.Cobranca;
import com.system.controleDeRegistrosFinanceiros.cobranca.repository.CobrancaRepository;
import com.system.controleDeRegistrosFinanceiros.relatorio.model.DadosRelatorio;

import java.io.InputStream;
import java.net.URI;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class RelatorioService {

    @Value("${SUPABASE_URL}")
    private String supabaseUrl;

    @Value("${SUPABASE_KEY}")
    private String supabaseKey;

    @Value("${BUCKET_NAME}")
    private String bucketName;

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    private final CobrancaRepository cobrancaRepository;
    private final ResourceLoader resourceLoader;
    private final DadosRelatorioMapper dadosRelatorioMapper;

    public RelatorioService(
            CobrancaRepository cobrancaRepository,
            ResourceLoader resourceLoader,
            DadosRelatorioMapper dadosRelatorioMapper) {
        this.cobrancaRepository = cobrancaRepository;
        this.resourceLoader = resourceLoader;
        this.dadosRelatorioMapper = dadosRelatorioMapper;
    }

    public byte[] gerarRelatorioDeCobrancas(CobrancaQueryFilters filters) throws Exception {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return gerarRelatorio(filters, user.getName());
    }

    public byte[] gerarRelatorioParaWhatsapp(CobrancaQueryFilters filters, String nomeUsuarioBot) throws Exception {
        return gerarRelatorio(filters, nomeUsuarioBot);
    }

    private byte[] gerarRelatorio(CobrancaQueryFilters filters, String nomeEmitente) throws Exception {
        List<DadosRelatorio> dadosRelatorio = geraDadosDoRelatorio(filters);

        Resource resource = resourceLoader.getResource("classpath:reports/cobranca_report.jasper");
        
        try (InputStream inputStream = resource.getInputStream()) {
            Resource logoResource = resourceLoader.getResource("classpath:images/Logo_branca.png");
            byte[] logoBytes = logoResource.getContentAsByteArray();

            double totalPix = dadosRelatorio.stream().mapToDouble(d -> d.getValorPix() != null ? d.getValorPix() : 0).sum();
            double totalVale = dadosRelatorio.stream().mapToDouble(d -> d.getValorVale() != null ? d.getValorVale() : 0).sum();
            double totalEspecie = dadosRelatorio.stream().mapToDouble(d -> d.getValorEspecie() != null ? d.getValorEspecie() : 0).sum();
            double totalFinal = dadosRelatorio.stream().mapToDouble(d -> d.getValorTotal() != null ? d.getValorTotal() : 0).sum();

            totalPix = Math.round(totalPix * 100.0) / 100.0;
            totalVale = Math.round(totalVale * 100.0) / 100.0;
            totalEspecie = Math.round(totalEspecie * 100.0) / 100.0;
            totalFinal = Math.round(totalFinal * 100.0) / 100.0;

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("PERIODO", LocalDate.now());
            parameters.put("DATA_EMISSAO", LocalDate.now());
            parameters.put("USUARIO_EMITENTE", nomeEmitente);
            parameters.put("REPORT_LOCALE", Locale.of("pt", "BR"));
            parameters.put("LOGO", logoBytes);
            parameters.put("TOTAL_FINAL", totalFinal);
            parameters.put("TOTAL_PIX", totalPix);
            parameters.put("TOTAL_VALE", totalVale);
            parameters.put("TOTAL_ESPECIE", totalEspecie);

            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(dadosRelatorio);
            JasperPrint jasperPrint = JasperFillManager.fillReport(inputStream, parameters, dataSource);

            return JasperExportManager.exportReportToPdf(jasperPrint);
        }
    }

    public List<DadosRelatorio> geraDadosDoRelatorio(CobrancaQueryFilters filters) {
        List<Cobranca> cobrancasFiltradas = cobrancaRepository.findAll(filters.toEspecification());

        if (cobrancasFiltradas.isEmpty()) {
            throw new ResourceNotFoundException(
                    "Não foi possível encontrar dados de cobrança dentro do período informado!"
            );
        }

        return cobrancasFiltradas.stream()
                .map(dadosRelatorioMapper::toRelatorio)
                .toList();
    }

    public String fazerUploadParaSupabase(byte[] pdfBytes, String nomeArquivo) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String urlString = supabaseUrl + "/storage/v1/object/" + bucketName + "/" + nomeArquivo;
            URI uri = new URI(urlString);

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + supabaseKey);
            headers.set("apikey", supabaseKey);
            headers.setContentType(MediaType.APPLICATION_PDF);

            HttpEntity<byte[]> entity = new HttpEntity<>(pdfBytes, headers);
            
            try {
                restTemplate.postForEntity(uri, entity, String.class);
            } catch (org.springframework.web.client.HttpClientErrorException.Conflict e) {
                System.out.println(">>> Arquivo já existe no Supabase, ignorando upload.");
            }
            return supabaseUrl + "/storage/v1/object/public/" + bucketName + "/" + nomeArquivo;

        } catch (Exception e) {
            System.err.println(">>> Erro real no Supabase: " + e.getMessage());
            return null;
        }
    }

    public void agendarExclusaoDocumentoSupabase(String nomeArquivo, long minutos) {
        scheduler.schedule(() -> {
            deletarArquivoNoSupabase(nomeArquivo);
        }, minutos, TimeUnit.MINUTES);
    }

    private void deletarArquivoNoSupabase(String nomeArquivo) {
        try {
            RestTemplate restTemplate = new RestTemplate();

            String url = supabaseUrl + "/storage/v1/object/" + bucketName + "/" + nomeArquivo;

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + supabaseKey);
            headers.set("apikey", supabaseKey);

            HttpEntity<Void> entity = new HttpEntity<>(headers);

            restTemplate.exchange(url, HttpMethod.DELETE, entity, String.class);
            System.out.println(">>> Arquivo removido do Supabase: " + nomeArquivo);
        } catch (Exception e) {
            System.err.println("Erro ao deletar arquivo: " + e.getMessage());
        }
    }
}