package com.system.controleDeRegistrosFinanceiros.veiculo.controller;

import com.system.controleDeRegistrosFinanceiros.cidade.model.dto.CidadeDTO;
import com.system.controleDeRegistrosFinanceiros.veiculo.model.dto.VeiculoDTO;
import com.system.controleDeRegistrosFinanceiros.veiculo.service.VeiculoService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/veiculos")
public class VeiculosController {

    private final VeiculoService veiculosService;

    public VeiculosController(VeiculoService veiculosService) {
        this.veiculosService = veiculosService;
    }

    @PostMapping
    public ResponseEntity<VeiculoDTO> criar(@RequestBody VeiculoDTO veiculos){
        return ResponseEntity.ok(veiculosService.criar(veiculos));
    };

    @GetMapping
    public ResponseEntity<List<VeiculoDTO>> buscaTodos() {
        return ResponseEntity.ok(veiculosService.buscaTodos());
    }

    @PutMapping
    public ResponseEntity<VeiculoDTO> editar(@RequestBody VeiculoDTO veiculo) {
        return ResponseEntity.ok(veiculosService.editar(veiculo));
    }

    @PatchMapping("/{id}/toggle")
    public ResponseEntity<Void> toggleStatus(@PathVariable Long id) {
        veiculosService.toggleStatus(id);
        return ResponseEntity.noContent().build();
    }
}
