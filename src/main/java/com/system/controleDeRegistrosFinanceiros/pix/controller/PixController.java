package com.system.controleDeRegistrosFinanceiros.pix.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import com.system.controleDeRegistrosFinanceiros.exceptions.ResourceNotFoundException;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
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
    private static final String DIRETORIO_PIX = "C:\\Users\\Rian\\Downloads\\CaminhoPix";
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
            @RequestPart(value = "comprovante", required = false) MultipartFile comprovante
    ) {
        return ResponseEntity.ok(pixService.salvar(pix, comprovante));
    }

    @GetMapping("/comprovante/{nome}")
    public ResponseEntity<byte[]> visualizarComprovante(@PathVariable String nome) {


        Path caminho = Paths.get(DIRETORIO_PIX).resolve(nome);

        if (!Files.exists(caminho)) {
            throw new ResourceNotFoundException("Comprovante", "Nome", nome);
        }

        try {
            byte[] bytes = Files.readAllBytes(caminho);
            String contentType = Files.probeContentType(caminho);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "inline; filename=\"" + nome + "\"")
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(bytes);

        } catch (IOException e) {
            throw new RuntimeException("Erro ao carregar comprovante", e);
        }
    }


    @DeleteMapping
    public ResponseEntity<Void> excluir(Long id){
        pixService.excluir(id);
        return ResponseEntity.noContent().build();
    }


}
