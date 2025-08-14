package com.system.controleDeRegistrosFinanceiros.cobranca.controller.impl;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.system.controleDeRegistrosFinanceiros.cobranca.controller.interfaces.CobrancasController;
import com.system.controleDeRegistrosFinanceiros.cobranca.model.dto.CobrancaDTO;
import com.system.controleDeRegistrosFinanceiros.cobranca.service.interfaces.CobrancasService;


@RestController
public class CobrancasControllerImpl implements CobrancasController {

    private final CobrancasService cobrancasService;

    // Injeção via construtor (boa prática)
    public CobrancasControllerImpl(CobrancasService cobrancasService) {
        this.cobrancasService = cobrancasService;
    }

    @Override
    public ResponseEntity<List<CobrancaDTO>> getTodos() {
        return ResponseEntity.ok(cobrancasService.getTodos());
    }

    @Override
    public ResponseEntity<CobrancaDTO> buscaPorId(Long id) {
        return ResponseEntity.ok(cobrancasService.buscarPorId(id));
    }

    @Override
    public ResponseEntity<CobrancaDTO> salvar(CobrancaDTO cobranca) {
        return ResponseEntity.ok(cobrancasService.salvar(cobranca));
    }

}