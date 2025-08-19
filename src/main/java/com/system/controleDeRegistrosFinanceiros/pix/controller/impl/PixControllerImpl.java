package com.system.controleDeRegistrosFinanceiros.pix.controller.impl;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.system.controleDeRegistrosFinanceiros.pix.controller.interfaces.PixController;
import com.system.controleDeRegistrosFinanceiros.pix.model.dto.PixDTO;
import com.system.controleDeRegistrosFinanceiros.pix.service.interfaces.PixService;

@RestController
public class PixControllerImpl implements PixController {
    
    PixService pixService;

    public PixControllerImpl(PixService pixService){
        this.pixService = pixService;
    }

    @Override
    public ResponseEntity<List<PixDTO>> buscaTodos() {
        return ResponseEntity.ok(pixService.buscaTodos());
    }

    @Override
    public ResponseEntity<PixDTO> buscaPorId(Long id) {
        return ResponseEntity.ok(pixService.buscarPorId(id));
    }

    @Override
    public ResponseEntity<PixDTO> salvar(PixDTO pix) {
        return ResponseEntity.ok(pixService.salvar(pix));
    }

    @Override
    public ResponseEntity<Void> excluir(Long id){
        pixService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<PixDTO> editar(PixDTO pix) {
        return ResponseEntity.ok(pixService.editar(pix));
    }

}
