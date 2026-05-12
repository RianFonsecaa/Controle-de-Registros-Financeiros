package com.system.controleDeRegistrosFinanceiros.funcionario.controller;

import com.system.controleDeRegistrosFinanceiros.cidade.model.dto.CidadeDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.system.controleDeRegistrosFinanceiros.funcionario.model.dto.FuncionarioDTO;
import com.system.controleDeRegistrosFinanceiros.funcionario.service.FuncionarioService;

import java.util.List;


@RestController
@RequestMapping("/funcionarios")
public class FuncionariosController {

    private final FuncionarioService funcionarioService;

    public FuncionariosController(FuncionarioService funcionarioService) {
        this.funcionarioService = funcionarioService;
    }

    @PostMapping
    public ResponseEntity<FuncionarioDTO> criar(@RequestBody FuncionarioDTO funcionario){
    return ResponseEntity.ok(funcionarioService.criar(funcionario));
    };

    @GetMapping
    public ResponseEntity<List<FuncionarioDTO>> buscaTodos(){
        return ResponseEntity.ok(funcionarioService.buscaTodos());
    }

    @PutMapping
    public ResponseEntity<FuncionarioDTO> editar(@RequestBody FuncionarioDTO funcionario) {
        return ResponseEntity.ok(funcionarioService.editar(funcionario));
    }

    @PatchMapping("/{id}/toggle")
    public ResponseEntity<Void> toggleStatus(@PathVariable Long id) {
        funcionarioService.toggleStatus(id);
        return ResponseEntity.noContent().build();
    }
}
