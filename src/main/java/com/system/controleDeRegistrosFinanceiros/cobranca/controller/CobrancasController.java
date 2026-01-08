package com.system.controleDeRegistrosFinanceiros.cobranca.controller;

import java.util.List;

import com.system.controleDeRegistrosFinanceiros.cobranca.model.dto.CobrancaQueryFilters;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    @GetMapping("/buscaPorFiltro")
    public ResponseEntity<List<CobrancaDTO>> buscaTodosPorFiltro(CobrancaQueryFilters filters) {
        return ResponseEntity.ok(cobrancasService.buscaTodosPorFiltro(filters));
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