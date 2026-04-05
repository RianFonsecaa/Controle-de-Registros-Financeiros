package com.system.controleDeRegistrosFinanceiros.vale.controller;

import com.system.controleDeRegistrosFinanceiros.pix.model.dto.PixDTO;
import com.system.controleDeRegistrosFinanceiros.pix.model.dto.PixQueryFilters;
import com.system.controleDeRegistrosFinanceiros.vale.model.dto.ValeDTO;
import com.system.controleDeRegistrosFinanceiros.vale.model.dto.ValeQueryFilters;
import com.system.controleDeRegistrosFinanceiros.vale.service.ValeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vales")
public class ValeController{

    ValeService valeService;

    public ValeController(ValeService valeService){
        this.valeService = valeService;
    }

    @GetMapping
    public ResponseEntity<List<ValeDTO>> buscaTodos() {
        return ResponseEntity.ok(valeService.buscaTodos());
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

    @GetMapping("/buscaPorFiltro")
    public ResponseEntity<List<ValeDTO>> buscaTodosPorFiltro(ValeQueryFilters filters) {
        return ResponseEntity.ok(valeService.buscaTodosPorFiltro(filters));
    }

    @PutMapping
    public ResponseEntity<ValeDTO> editar(@RequestBody ValeDTO vale) {
        return ResponseEntity.ok(valeService.editar(vale));
    }

}