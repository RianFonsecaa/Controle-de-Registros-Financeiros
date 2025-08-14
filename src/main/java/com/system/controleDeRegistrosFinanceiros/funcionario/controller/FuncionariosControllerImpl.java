package com.system.controleDeRegistrosFinanceiros.funcionario.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.system.controleDeRegistrosFinanceiros.funcionario.model.FuncionarioDTO;
import com.system.controleDeRegistrosFinanceiros.funcionario.service.FuncionarioService;



@RestController
public class FuncionariosControllerImpl implements FuncionariosController {

    private final FuncionarioService funcionarioService;

    public FuncionariosControllerImpl(FuncionarioService funcionarioService) {
        this.funcionarioService = funcionarioService;
    }


    @Override
    public ResponseEntity<FuncionarioDTO> criar(@RequestBody FuncionarioDTO funcionario){
    return ResponseEntity.ok(funcionarioService.criar(funcionario));
    };
    
}
