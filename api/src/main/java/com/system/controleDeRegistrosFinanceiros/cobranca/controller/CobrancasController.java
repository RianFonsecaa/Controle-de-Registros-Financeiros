package com.system.controleDeRegistrosFinanceiros.cobranca.controller;

import java.util.List;

import com.system.controleDeRegistrosFinanceiros.cobranca.model.dto.CobrancaQueryFilters;

import org.hibernate.query.SortDirection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
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
   public ResponseEntity<Page<CobrancaDTO>> buscaTodos(
        @PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(cobrancasService.buscaTodos(pageable));
    }

    @GetMapping("/buscaPorFiltro")
    public ResponseEntity<Page<CobrancaDTO>> buscaTodosPorFiltro( 
        @PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.DESC) Pageable pageable, 
        CobrancaQueryFilters filters
    ) {
        return ResponseEntity.ok(cobrancasService.buscaTodosPorFiltro(pageable, filters));
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