package com.system.controleDeRegistrosFinanceiros.relatorio.service;


import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import com.system.controleDeRegistrosFinanceiros.cobranca.model.entity.Cobranca;
import com.system.controleDeRegistrosFinanceiros.relatorio.model.CobrancaDiaria;

import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Serviço responsável pela lógica de geração do relatório principal de Cobrança.
 */
@Service
public class RelatorioService {

    @Autowired
    private ResourceLoader resourceLoader;

    public void gerarRelatorioCobranca(List<CobrancaDiaria> cobrancaDiaria) throws JRException {

        try {
            // 1. CAMINHO DO ARQUIVO JRXML PRINCIPAL
            String pathRelatorioPrincipal = "classpath:reports/cobranca-report.jrxml";

            // 2. COMPILAR O RELATÓRIO PRINCIPAL (.jrxml -> .jasper)
            JasperReport relatorioPrincipal = compilarRelatorio(pathRelatorioPrincipal);
            String pathCabecalho = "classpath:images/CABECALHO.png";
            Double somatorioTotalGeral = cobrancaDiaria.stream()
                                                        .mapToDouble(CobrancaDiaria::getValorTotal)
                                                        .sum();

            // 3. PREPARAR PARÂMETROS
            // O mapa de parâmetros agora é mais simples, contendo apenas informações gerais.
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("DATA_GERACAO", java.time.LocalDate.now());
            parameters.put("REPORT_LOCALE", Locale.of("pt", "BR"));
            parameters.put("CABECALHO", pathCabecalho);
            parameters.put("SOMATORIO_TOTAL_GERAL", somatorioTotalGeral);

            // 4. CRIAR A FONTE DE DADOS (DataSource)
            // A lógica aqui é a mesma: envolvemos nosso objeto em uma lista para o Jasper.
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(cobrancaDiaria);

            // 5. PREENCHER O RELATÓRIO
            JasperPrint jasperPrint = JasperFillManager.fillReport(relatorioPrincipal, parameters, dataSource);

            // 6. EXPORTAR PARA PDF
            // PARA:
            String caminhoDeSaida = "C:\\Users\\Administrador\\Desktop\\REPORTS\\meu_relatorio_de_teste.pdf";
            JasperExportManager.exportReportToPdfFile(jasperPrint, caminhoDeSaida);

        } catch (Exception e) {
            e.printStackTrace();
            throw new JRException("Erro ao gerar o relatório de cobrança: " + e.getMessage(), e);
        }
    }

    /**
     * Método auxiliar para carregar e compilar um arquivo .jrxml a partir do classpath.
     */
    private JasperReport compilarRelatorio(String path) throws Exception {
        final Resource resource = resourceLoader.getResource(path);
        final InputStream inputStream = resource.getInputStream();
        return JasperCompileManager.compileReport(inputStream);
    }
}