package com.system.controleDeRegistrosFinanceiros.cidade.controller.impl;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.system.controleDeRegistrosFinanceiros.cidade.controller.interfaces.CidadesController;
import com.system.controleDeRegistrosFinanceiros.cidade.model.dto.CidadeDTO;
import com.system.controleDeRegistrosFinanceiros.cidade.service.interfaces.CidadeService;


@RestController
public class CidadesControllerImpl implements CidadesController {

    private final CidadeService cidadeService;

    public CidadesControllerImpl(CidadeService cidadeService) {
        this.cidadeService = cidadeService;
    }


    @Override
    public ResponseEntity<CidadeDTO> criar(@RequestBody CidadeDTO cidade){
    return ResponseEntity.ok(cidadeService.criar(cidade));
    };
    
}
