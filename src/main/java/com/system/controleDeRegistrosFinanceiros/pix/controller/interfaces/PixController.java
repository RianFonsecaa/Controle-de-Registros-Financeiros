package com.system.controleDeRegistrosFinanceiros.pix.controller.interfaces;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.system.controleDeRegistrosFinanceiros.pix.model.dto.PixDTO;

@RequestMapping("/pix")
public interface PixController {
    
    @PostMapping
    ResponseEntity<PixDTO> salvar(@RequestBody PixDTO pixDto);

    @GetMapping
    ResponseEntity<List<PixDTO>> buscaTodos();

    @GetMapping("/{id}")
    ResponseEntity<PixDTO> buscaPorId(@RequestParam Long id);
    
    @PutMapping
    ResponseEntity<PixDTO> editar(@RequestBody PixDTO pixDto);

    @DeleteMapping
    ResponseEntity<Void> excluir(@RequestParam Long id);
}
