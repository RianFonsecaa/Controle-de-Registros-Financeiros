package com.system.controleDeRegistrosFinanceiros.relatorio.controller.impl;


import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.system.controleDeRegistrosFinanceiros.relatorio.controller.interfaces.RelatorioController;
import com.system.controleDeRegistrosFinanceiros.relatorio.service.interfaces.RelatorioService;

@RestController
public class RelatorioControllerImpl implements RelatorioController {

    RelatorioService relatorioService;

    public RelatorioControllerImpl(RelatorioService relatorioService){
        this.relatorioService = relatorioService;
    }
    

    @Override
    public ResponseEntity<byte[]> gerarRelatorioDiarioDeCobrancas() throws Exception{

        byte[] relatorioPdf = relatorioService.gerarRelatorioCobranca();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "Relatório Diário de Cobranças " + java.time.LocalDate.now() + ".pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .body(relatorioPdf);
    };
}
