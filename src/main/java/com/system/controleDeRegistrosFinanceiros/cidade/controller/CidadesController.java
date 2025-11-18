package com.system.controleDeRegistrosFinanceiros.cidade.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.system.controleDeRegistrosFinanceiros.cidade.model.dto.CidadeDTO;
import com.system.controleDeRegistrosFinanceiros.cidade.service.CidadeService;


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

    @DeleteMapping("/{id}")
    public ResponseEntity excluir(@PathVariable Long id) {
        cidadeService.excluir(id);
        return ResponseEntity.noContent().build();
    }
    
}
