package com.system.controleDeRegistrosFinanceiros.funcionario.controller;

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
    

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id){
        return ResponseEntity.noContent().build();
    }
}
