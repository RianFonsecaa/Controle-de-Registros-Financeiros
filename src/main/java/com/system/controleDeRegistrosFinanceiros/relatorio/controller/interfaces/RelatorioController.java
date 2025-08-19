package com.system.controleDeRegistrosFinanceiros.relatorio.controller.interfaces;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/relatorios")
public interface RelatorioController {
    
    @GetMapping("/cobranca-diaria")
    ResponseEntity<byte[]> gerarRelatorioDiarioDeCobrancas() throws Exception;
}
