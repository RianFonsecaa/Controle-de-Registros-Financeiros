package com.system.controleDeRegistrosFinanceiros.pix.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import com.system.controleDeRegistrosFinanceiros.cobranca.model.dto.CobrancaDTO;
import com.system.controleDeRegistrosFinanceiros.cobranca.model.dto.CobrancaQueryFilters;
import com.system.controleDeRegistrosFinanceiros.exceptions.ResourceNotFoundException;
import com.system.controleDeRegistrosFinanceiros.pix.model.dto.PixQueryFilters;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
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
    public ResponseEntity<Resource> visualizarComprovante(@PathVariable String nome) {
        try {
            Path caminho = Paths.get(DIRETORIO_PIX).resolve(nome).normalize();
            Resource recurso = new UrlResource(caminho.toUri());

            if (!recurso.exists() || !recurso.isReadable()) {
                throw new ResourceNotFoundException("Comprovante", "Nome", nome);
            }

            String contentType = Files.probeContentType(caminho);
            if (contentType == null) contentType = "application/octet-stream";

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + recurso.getFilename() + "\"")
                    .body(recurso);

        } catch (IOException e) {

            throw new RuntimeException("Erro ao carregar comprovante", e);
        }
    }

    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PixDTO> editar(
            @RequestPart("pix") PixDTO pixDTO,
            @RequestPart(value = "comprovante", required = false) MultipartFile comprovante
    ) {
        return ResponseEntity.ok(pixService.editar(pixDTO, comprovante));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id){
        pixService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/buscaPorFiltro")
    public ResponseEntity<List<PixDTO>> buscaTodosPorFiltro(PixQueryFilters filters) {
        return ResponseEntity.ok(pixService.buscaTodosPorFiltro(filters));
    }

    @GetMapping("/porCobranca/{cobrancaId}")
    public ResponseEntity<List<PixDTO>> buscaPorCobranca(@PathVariable Long cobrancaId){
        return ResponseEntity.ok(pixService.buscarPorCobranca(cobrancaId));
    }

@GetMapping("/comprovantes/{nomeArquivo}")
    public ResponseEntity<Resource> getComprovante(@PathVariable String nomeArquivo) {
        Resource resource = pixService.carregarComprovante(nomeArquivo);
        
        String contentType = null;
        try {
            contentType = Files.probeContentType(Paths.get(resource.getFile().getAbsolutePath()));
        } catch (IOException e) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }


}
