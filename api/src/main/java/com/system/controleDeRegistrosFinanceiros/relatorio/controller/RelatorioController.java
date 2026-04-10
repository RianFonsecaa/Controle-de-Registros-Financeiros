package com.system.controleDeRegistrosFinanceiros.relatorio.controller;


import com.system.controleDeRegistrosFinanceiros.cobranca.model.dto.CobrancaQueryFilters;
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
    

    @GetMapping()
    public ResponseEntity<byte[]> gerarRelatorioDeCobrancas(CobrancaQueryFilters filters) throws Exception{
        byte[] relatorioPdf = relatorioService.gerarRelatorioDeCobrancas(filters);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "Relatório de Cobranças"+ ".pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .body(relatorioPdf);
    };
}
