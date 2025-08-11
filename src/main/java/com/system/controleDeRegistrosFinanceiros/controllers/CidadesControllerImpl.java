package com.system.controleDeRegistrosFinanceiros.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.system.controleDeRegistrosFinanceiros.controllers.interfaces.CidadesController;
import com.system.controleDeRegistrosFinanceiros.model.dtos.CidadeDTO;
import com.system.controleDeRegistrosFinanceiros.services.interfaces.CidadeService;

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
