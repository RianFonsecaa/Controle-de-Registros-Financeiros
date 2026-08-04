package com.system.controleDeRegistrosFinanceiros.vale.controller;

import com.system.controleDeRegistrosFinanceiros.vale.model.dto.ValeDTO;
import com.system.controleDeRegistrosFinanceiros.vale.model.dto.ValeQueryFilters;
import com.system.controleDeRegistrosFinanceiros.vale.service.ValeService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/vales")
public class ValeController{

    ValeService valeService;

    public ValeController(ValeService valeService){
        this.valeService = valeService;
    }

    @GetMapping
    public ResponseEntity<Page<ValeDTO>> buscaTodos( @PageableDefault(
        page = 0, size = 10, sort = "id", direction = Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(valeService.buscaTodos(pageable));
    }

    @GetMapping("/buscaPorFiltro")
    public ResponseEntity<Page<ValeDTO>> buscaTodosPorFiltro(
        @PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.DESC) Pageable pageable,
        ValeQueryFilters filters
    ) {
        return ResponseEntity.ok(valeService.buscaTodosPorFiltro(pageable, filters));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ValeDTO> buscaPorId(Long id) {
        return ResponseEntity.ok(valeService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<ValeDTO> salvar(@RequestBody ValeDTO vale) {
        return ResponseEntity.ok(valeService.salvar(vale));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id){
        valeService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<ValeDTO> editar(@RequestBody ValeDTO vale) {
        return ResponseEntity.ok(valeService.editar(vale));
    }

}