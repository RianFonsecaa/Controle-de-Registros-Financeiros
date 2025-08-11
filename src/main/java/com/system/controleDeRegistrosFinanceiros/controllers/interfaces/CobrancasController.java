package com.system.controleDeRegistrosFinanceiros.controllers.interfaces;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.system.controleDeRegistrosFinanceiros.model.dtos.CobrancaDTO;


@RequestMapping("/cobrancas")
public interface CobrancasController {

    @GetMapping
    ResponseEntity<List<CobrancaDTO>> getTodos();

    @GetMapping("/{id}")
    ResponseEntity<CobrancaDTO> buscaPorId(@PathVariable Long id);

    @PostMapping
    ResponseEntity<CobrancaDTO> salvar(@RequestBody CobrancaDTO cobranca);

    @PutMapping("/{id}")
    ResponseEntity<CobrancaDTO> atualizar(@PathVariable Long id, @RequestBody CobrancaDTO cobranca);

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deletar(@PathVariable Long id);
}
