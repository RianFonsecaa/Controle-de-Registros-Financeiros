package com.system.controleDeRegistrosFinanceiros.relatorio.service;


import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
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

    public byte[] gerarRelatorioCobranca() throws Exception {        

        
        List<CobrancaDiaria> dadosRelatorio = geraDadosDoRelatorio();

        final Resource resource = resourceLoader.getResource("classpath:reports/cobranca-report.jrxml");
        final InputStream inputStream = resource.getInputStream();
        JasperReport relatorioPrincipal = JasperCompileManager.compileReport(inputStream);

        Double somatorioTotalGeral = dadosRelatorio.stream()
                                                .mapToDouble(CobrancaDiaria::getValorTotal)
                                                .sum();

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("DATA_GERACAO", java.time.LocalDate.now());
        parameters.put("REPORT_LOCALE", Locale.of("pt", "BR"));
        parameters.put("CABECALHO", "classpath:images/CABECALHO.png");
        parameters.put("SOMATORIO_TOTAL_GERAL", somatorioTotalGeral);

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(dadosRelatorio);
        JasperPrint jasperPrint = JasperFillManager.fillReport(relatorioPrincipal, parameters, dataSource);

        return JasperExportManager.exportReportToPdf(jasperPrint);
    }

    public List<CobrancaDiaria> geraDadosDoRelatorio(){
            List<Cobranca> cobrancasDoDia = cobrancaRepository.findAllByData(LocalDate.now());
            System.out.println("Cobranças do dia: " + cobrancasDoDia.size());
            List<CobrancaDiaria> dadosRelatorio = cobrancasDoDia.stream()
                                                                     .map(CobrancaDiaria::new)
                                                                     .collect(Collectors.toList());
        return dadosRelatorio;
    }
}