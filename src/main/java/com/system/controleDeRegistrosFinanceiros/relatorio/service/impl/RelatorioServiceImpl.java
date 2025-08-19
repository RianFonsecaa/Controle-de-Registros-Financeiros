package com.system.controleDeRegistrosFinanceiros.relatorio.service.impl;


import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import com.system.controleDeRegistrosFinanceiros.cobranca.model.entity.Cobranca;
import com.system.controleDeRegistrosFinanceiros.cobranca.repository.CobrancaRepository;
import com.system.controleDeRegistrosFinanceiros.relatorio.model.CobrancaDiaria;
import com.system.controleDeRegistrosFinanceiros.relatorio.service.interfaces.RelatorioService;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Serviço responsável pela lógica de geração do relatório principal de Cobrança.
 */
@Service
public class RelatorioServiceImpl implements RelatorioService {

    private final CobrancaRepository cobrancaRepository;
    private final ResourceLoader resourceLoader;

    public RelatorioServiceImpl(CobrancaRepository cobrancaRepository, ResourceLoader resourceLoader){
        this.cobrancaRepository = cobrancaRepository;
        this.resourceLoader = resourceLoader;
    };


    @Override
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
            List<CobrancaDiaria> dadosRelatorio = cobrancasDoDia.stream()
                                                                     .map(CobrancaDiaria::new)
                                                                     .collect(Collectors.toList());
        return dadosRelatorio;
    }
}