package com.system.controleDeRegistrosFinanceiros.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.system.controleDeRegistrosFinanceiros.controllers.interfaces.FuncionariosController;
import com.system.controleDeRegistrosFinanceiros.model.dtos.FuncionarioDTO;
import com.system.controleDeRegistrosFinanceiros.services.interfaces.FuncionarioService;

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
