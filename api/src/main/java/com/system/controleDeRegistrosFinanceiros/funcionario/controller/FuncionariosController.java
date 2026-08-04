package com.system.controleDeRegistrosFinanceiros.funcionario.controller;

import com.system.controleDeRegistrosFinanceiros.cidade.model.dto.CidadeDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.system.controleDeRegistrosFinanceiros.funcionario.model.dto.FuncionarioDTO;
import com.system.controleDeRegistrosFinanceiros.funcionario.service.FuncionarioService;
import com.system.controleDeRegistrosFinanceiros.usuario.model.UsuarioDTO;

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
    public ResponseEntity<Page<FuncionarioDTO>> buscaTodos(
        @RequestParam(required = false) Boolean ativo,
        @PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.DESC) Pageable pageable
    ){
        return ResponseEntity.ok(funcionarioService.buscaTodos(ativo, pageable));
    }

    @GetMapping("/ativos")
    public ResponseEntity<List<FuncionarioDTO>> buscaAtivos(){
        return ResponseEntity.ok(funcionarioService.buscaAtivos());
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
