package com.system.controleDeRegistrosFinanceiros.relatorio.service;
import com.system.controleDeRegistrosFinanceiros.authentication.model.User;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.system.controleDeRegistrosFinanceiros.cobranca.model.entity.Cobranca;
import com.system.controleDeRegistrosFinanceiros.cobranca.repository.CobrancaRepository;
import com.system.controleDeRegistrosFinanceiros.relatorio.model.CobrancaDiaria;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RelatorioService{

    private final CobrancaRepository cobrancaRepository;
    private final ResourceLoader resourceLoader;

    public RelatorioService(CobrancaRepository cobrancaRepository, ResourceLoader resourceLoader){
        this.cobrancaRepository = cobrancaRepository;
        this.resourceLoader = resourceLoader;
    };

    public byte[] gerarRelatorioDiarioDeCobranca() throws Exception {

        User user = (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        double totalPix = 0;
        double totalVale = 0;
        double totalEspecie = 0;
        double totalFinal = 0;

        List<CobrancaDiaria> dadosRelatorio = geraDadosDoRelatorio();

        final Resource resource = resourceLoader.getResource("classpath:reports/cobranca-report.jrxml");
        final InputStream inputStream = resource.getInputStream();
        JasperReport relatorioPrincipal = JasperCompileManager.compileReport(inputStream);

        for (CobrancaDiaria cobrancaDiaria : dadosRelatorio) {
            totalPix     += cobrancaDiaria.getValorPix();
            totalVale    += cobrancaDiaria.getValorVale();
            totalEspecie += cobrancaDiaria.getValorEspecie();
            totalFinal += cobrancaDiaria.getValorTotal();
        }

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("PERIODO", java.time.LocalDate.now());
        parameters.put("DATA_EMISSAO", java.time.LocalDate.now());
        parameters.put("USUARIO_EMITENTE", user.getName());
        parameters.put("REPORT_LOCALE", Locale.of("pt", "BR"));
        parameters.put("LOGO", "classpath:images/Logo_branca.png");
        parameters.put("TOTAL_FINAL", totalFinal);
        parameters.put("TOTAL_PIX", totalPix);
        parameters.put("TOTAL_VALE", totalVale);
        parameters.put("TOTAL_ESPECIE", totalEspecie);

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(dadosRelatorio);
        JasperPrint jasperPrint = JasperFillManager.fillReport(relatorioPrincipal, parameters, dataSource);

        return JasperExportManager.exportReportToPdf(jasperPrint);
    }

    public List<CobrancaDiaria> geraDadosDoRelatorio(){
            List<Cobranca> cobrancasDoDia = cobrancaRepository.findAllByData(LocalDate.now());
            List<CobrancaDiaria> dadosRelatorio = cobrancasDoDia.stream()
                                                                     .map(CobrancaDiaria::new)
                                                                     .collect(Collectors.toList());
        return dadosRelatorio;
    }
}