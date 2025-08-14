package com.system.controleDeRegistrosFinanceiros.cobranca.controller.interfaces;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.system.controleDeRegistrosFinanceiros.cobranca.model.dto.CobrancaDTO;



@RequestMapping("/cobrancas")
public interface CobrancasController {

    @GetMapping
    ResponseEntity<List<CobrancaDTO>> getTodos();

    @GetMapping("/{id}")
    ResponseEntity<CobrancaDTO> buscaPorId(@PathVariable Long id);

    @PostMapping
    ResponseEntity<CobrancaDTO> salvar(@RequestBody CobrancaDTO cobranca);

}
