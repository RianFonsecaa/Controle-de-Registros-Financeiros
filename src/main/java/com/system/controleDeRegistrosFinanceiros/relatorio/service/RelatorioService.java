package com.system.controleDeRegistrosFinanceiros.relatorio.service;
import com.system.controleDeRegistrosFinanceiros.authentication.model.User;
import com.system.controleDeRegistrosFinanceiros.exceptions.ResourceNotFoundException;
import com.system.controleDeRegistrosFinanceiros.relatorio.mapper.DadosRelatorioMapper;
import jakarta.persistence.EntityNotFoundException;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.system.controleDeRegistrosFinanceiros.cobranca.model.entity.Cobranca;
import com.system.controleDeRegistrosFinanceiros.cobranca.repository.CobrancaRepository;
import com.system.controleDeRegistrosFinanceiros.relatorio.model.DadosRelatorio;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RelatorioService {

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

    public byte[] gerarRelatorioDeCobrancas(LocalDate dataInicio, LocalDate dataFim) throws Exception {

        User user = (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        double totalPix = 0;
        double totalVale = 0;
        double totalEspecie = 0;
        double totalFinal = 0;

        List<DadosRelatorio> dadosRelatorio = geraDadosDoRelatorio(dataInicio, dataFim);

        Resource resource = resourceLoader.getResource("classpath:reports/cobranca-report.jrxml");
        InputStream inputStream = resource.getInputStream();
        JasperReport relatorioPrincipal = JasperCompileManager.compileReport(inputStream);

        for (DadosRelatorio dados : dadosRelatorio) {
            totalPix     += dados.getValorPix();
            totalVale    += dados.getValorVale();
            totalEspecie += dados.getValorEspecie();
            totalFinal   += dados.getValorTotal();
        }

        totalPix     = Math.round(totalPix * 100.0) / 100.0;
        totalVale    = Math.round(totalVale * 100.0) / 100.0;
        totalEspecie = Math.round(totalEspecie * 100.0) / 100.0;
        totalFinal   = Math.round(totalFinal * 100.0) / 100.0;

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("PERIODO", LocalDate.now());
        parameters.put("DATA_EMISSAO", LocalDate.now());
        parameters.put("USUARIO_EMITENTE", user.getName());
        parameters.put("REPORT_LOCALE", Locale.of("pt", "BR"));
        parameters.put("LOGO", "classpath:images/Logo_branca.png");
        parameters.put("TOTAL_FINAL", totalFinal);
        parameters.put("TOTAL_PIX", totalPix);
        parameters.put("TOTAL_VALE", totalVale);
        parameters.put("TOTAL_ESPECIE", totalEspecie);

        JRBeanCollectionDataSource dataSource =
                new JRBeanCollectionDataSource(dadosRelatorio);

        JasperPrint jasperPrint =
                JasperFillManager.fillReport(relatorioPrincipal, parameters, dataSource);

        return JasperExportManager.exportReportToPdf(jasperPrint);
    }

    public List<DadosRelatorio> geraDadosDoRelatorio(LocalDate dataInicio, LocalDate dataFim) {

        List<Cobranca> cobrancas = cobrancaRepository.findByDataBetween(dataInicio, dataFim);

        if (cobrancas.isEmpty()) {
            throw new ResourceNotFoundException(
                    "Não foi possível encontrar dados de cobrança dentro do período informado!"
            );
        }

        return cobrancas.stream()
                .map(dadosRelatorioMapper::toRelatorio)
                .toList();
    }
}