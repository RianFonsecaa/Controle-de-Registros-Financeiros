package com.system.controleDeRegistrosFinanceiros.veiculo.controller;

import com.system.controleDeRegistrosFinanceiros.cidade.model.dto.CidadeDTO;
import com.system.controleDeRegistrosFinanceiros.usuario.model.UsuarioDTO;
import com.system.controleDeRegistrosFinanceiros.veiculo.model.dto.VeiculoDTO;
import com.system.controleDeRegistrosFinanceiros.veiculo.service.VeiculoService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
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
    public ResponseEntity<Page<VeiculoDTO>> buscaTodos( @RequestParam(required = false) Boolean ativo,
        @PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(veiculosService.buscaTodos(ativo, pageable));
    }

    @GetMapping("/ativos")
    public ResponseEntity<List<VeiculoDTO>> buscaAtivos(){
        return ResponseEntity.ok(veiculosService.buscaAtivos());
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
