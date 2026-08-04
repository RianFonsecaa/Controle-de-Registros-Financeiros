package com.system.controleDeRegistrosFinanceiros.cidade.controller;

import com.system.controleDeRegistrosFinanceiros.cidade.model.entity.Cidade;
import com.system.controleDeRegistrosFinanceiros.cobranca.model.dto.CobrancaDTO;
import com.system.controleDeRegistrosFinanceiros.usuario.model.UsuarioDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
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
    }

    @GetMapping
    public ResponseEntity<Page<CidadeDTO>> buscaTodos(
        @RequestParam(required = false, defaultValue = "true") Boolean ativo, // <-- defaultValue ajuda a evitar NullPointerException se vier vazio
        @PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(cidadeService.buscaTodos(ativo, pageable));
    }

    @GetMapping("/ativas")
    public ResponseEntity<List<CidadeDTO>> buscaAtivas() {
        return ResponseEntity.ok(cidadeService.buscaAtivas());
    }

    @PutMapping
    public ResponseEntity<CidadeDTO> editar(@RequestBody CidadeDTO cidade) {
        System.out.println(cidade.getAtivo());
        return ResponseEntity.ok(cidadeService.editar(cidade));
    }

    @PatchMapping("/{id}/toggle")
    public ResponseEntity<Void> toggleStatus(@PathVariable Long id) {
        cidadeService.toggleStatus(id);
        return ResponseEntity.noContent().build();
    }
    
}
