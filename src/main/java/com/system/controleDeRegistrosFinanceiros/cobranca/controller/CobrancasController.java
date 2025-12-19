package com.system.controleDeRegistrosFinanceiros.cobranca.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.system.controleDeRegistrosFinanceiros.cobranca.model.dto.CobrancaDTO;
import com.system.controleDeRegistrosFinanceiros.cobranca.service.CobrancasService;


@RestController
@RequestMapping("/cobrancas")
public class CobrancasController{

    private final CobrancasService cobrancasService;

    public CobrancasController(CobrancasService cobrancasService) {
        this.cobrancasService = cobrancasService;
    }

    @GetMapping
    public ResponseEntity<List<CobrancaDTO>> buscaTodos() {

        return ResponseEntity.ok(cobrancasService.buscaTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CobrancaDTO> buscaPorId(@PathVariable Long id) {
        return ResponseEntity.ok(cobrancasService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<CobrancaDTO> salvar(@RequestBody CobrancaDTO cobranca) {
        return ResponseEntity.ok(cobrancasService.salvar(cobranca));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id){
        cobrancasService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<CobrancaDTO> editar(@RequestBody CobrancaDTO cobranca) {
        return ResponseEntity.ok(cobrancasService.editar(cobranca));
    }

}