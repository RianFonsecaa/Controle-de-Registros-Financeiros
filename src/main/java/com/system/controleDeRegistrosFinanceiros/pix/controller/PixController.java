package com.system.controleDeRegistrosFinanceiros.pix.controller;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import com.system.controleDeRegistrosFinanceiros.pix.model.dto.PixDTO;
import com.system.controleDeRegistrosFinanceiros.pix.service.PixService;
import org.springframework.web.multipart.MultipartFile;

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

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PixDTO> salvar(
            @RequestPart("pix") PixDTO pix,
            @RequestPart(value = "comprovante", required = false) MultipartFile arquivo
    ) {
        return ResponseEntity.ok(pixService.salvar(pix, arquivo));
    }


    @DeleteMapping
    public ResponseEntity<Void> excluir(Long id){
        pixService.excluir(id);
        return ResponseEntity.noContent().build();
    }


}
