package com.system.controleDeRegistrosFinanceiros.funcionario.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.system.controleDeRegistrosFinanceiros.funcionario.model.dto.FuncionarioDTO;
import com.system.controleDeRegistrosFinanceiros.funcionario.service.FuncionarioService;



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
    

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id){
        return ResponseEntity.noContent().build();
    }
}
