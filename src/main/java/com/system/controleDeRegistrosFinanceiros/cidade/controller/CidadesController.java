package com.system.controleDeRegistrosFinanceiros.cidade.controller;

import com.system.controleDeRegistrosFinanceiros.cidade.model.entity.Cidade;
import com.system.controleDeRegistrosFinanceiros.cobranca.model.dto.CobrancaDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.system.controleDeRegistrosFinanceiros.cidade.model.dto.CidadeDTO;
import com.system.controleDeRegistrosFinanceiros.cidade.service.CidadeService;

import java.util.List;


@RestController
@RequestMapping("/cidades")
public class CidadesController {

    private final CidadeService cidadeService;

    public CidadesController(CidadeService cidadeService) {
        this.cidadeService = cidadeService;
    }

    @PostMapping
    public ResponseEntity<CidadeDTO> criar(@RequestBody CidadeDTO cidade){
        return ResponseEntity.ok(cidadeService.criar(cidade));
    };

    @GetMapping
    public ResponseEntity<List<CidadeDTO>> buscaTodos() {
        return ResponseEntity.ok(cidadeService.buscaTodos());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity excluir(@PathVariable Long id) {
        cidadeService.excluir(id);
        return ResponseEntity.noContent().build();
    }
    
}
