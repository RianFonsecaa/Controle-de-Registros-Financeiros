package com.system.controleDeRegistrosFinanceiros.funcionario.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.system.controleDeRegistrosFinanceiros.funcionario.model.FuncionarioDTO;


@RequestMapping("/funcionarios")
public interface FuncionariosController {
    
    @PostMapping
    ResponseEntity<FuncionarioDTO> criar(@RequestBody FuncionarioDTO funcionario);

}