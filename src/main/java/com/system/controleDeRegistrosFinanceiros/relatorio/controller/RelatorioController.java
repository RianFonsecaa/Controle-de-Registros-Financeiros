package com.system.controleDeRegistrosFinanceiros.relatorio.controller;


import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.system.controleDeRegistrosFinanceiros.relatorio.service.RelatorioService;

@RestController
@RequestMapping("/relatorios")
public class RelatorioController {

    RelatorioService relatorioService;

    public RelatorioController(RelatorioService relatorioService){
        this.relatorioService = relatorioService;
    }
    

    @GetMapping("/cobranca-diaria")
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
