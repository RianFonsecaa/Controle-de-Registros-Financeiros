package com.system.controleDeRegistrosFinanceiros.controllers.interfaces;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.system.controleDeRegistrosFinanceiros.model.dtos.CidadeDTO;

@RequestMapping("/cidades")
public interface CidadesController {
    
    @PostMapping
    ResponseEntity<CidadeDTO> criar(@RequestBody CidadeDTO cidade);

}
