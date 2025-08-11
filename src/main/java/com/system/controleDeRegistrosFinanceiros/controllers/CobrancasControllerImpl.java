package com.system.controleDeRegistrosFinanceiros.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.system.controleDeRegistrosFinanceiros.controllers.interfaces.CobrancasController;
import com.system.controleDeRegistrosFinanceiros.model.dtos.CobrancaDTO;
import com.system.controleDeRegistrosFinanceiros.services.interfaces.CobrancasService;

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

    @Override
    public ResponseEntity<CobrancaDTO> atualizar(Long id, CobrancaDTO cobranca) {
        return ResponseEntity.ok(cobrancasService.atualizar(id, cobranca));
    }

    @Override
    public ResponseEntity<Void> deletar(Long id) {
        cobrancasService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}