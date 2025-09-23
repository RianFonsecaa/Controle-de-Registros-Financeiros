package com.system.controleDeRegistrosFinanceiros.pix.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.system.controleDeRegistrosFinanceiros.pix.model.dto.PixDTO;
import com.system.controleDeRegistrosFinanceiros.pix.service.PixService;

@RestController
@RequestMapping("/pix")
public class PixController{
    
    PixService pixService;

    public PixController(PixService pixService){
        this.pixService = pixService;
    }

    @GetMapping
    public ResponseEntity<List<PixDTO>> buscaTodos() {
        return ResponseEntity.ok(pixService.buscaTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PixDTO> buscaPorId(Long id) {
        return ResponseEntity.ok(pixService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<PixDTO> salvar(@RequestBody PixDTO pix) {
        return ResponseEntity.ok(pixService.salvar(pix));
    }

    @DeleteMapping
    public ResponseEntity<Void> excluir(Long id){
        pixService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<PixDTO> editar(@RequestBody PixDTO pix) {
        return ResponseEntity.ok(pixService.editar(pix));
    }

}
