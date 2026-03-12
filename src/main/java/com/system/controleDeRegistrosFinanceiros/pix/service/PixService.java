package com.system.controleDeRegistrosFinanceiros.pix.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import com.system.controleDeRegistrosFinanceiros.cidade.service.CidadeService;
import com.system.controleDeRegistrosFinanceiros.cobranca.model.dto.CobrancaDTO;
import com.system.controleDeRegistrosFinanceiros.cobranca.model.dto.CobrancaQueryFilters;
import com.system.controleDeRegistrosFinanceiros.cobranca.service.CobrancasService;
import com.system.controleDeRegistrosFinanceiros.exceptions.ResourceNotFoundException;
import com.system.controleDeRegistrosFinanceiros.pix.model.dto.PixQueryFilters;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.system.controleDeRegistrosFinanceiros.cidade.model.entity.Cidade;
import com.system.controleDeRegistrosFinanceiros.cidade.repository.CidadeRepository;
import com.system.controleDeRegistrosFinanceiros.cobranca.model.entity.Cobranca;
import com.system.controleDeRegistrosFinanceiros.pix.mapper.PixMapper;
import com.system.controleDeRegistrosFinanceiros.pix.model.dto.PixDTO;
import com.system.controleDeRegistrosFinanceiros.pix.model.entity.Pix;
import com.system.controleDeRegistrosFinanceiros.pix.repository.PixRepository;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.web.multipart.MultipartFile;

@Service
public class PixService{

    private static final String DIRETORIO_PIX = "C:\\Users\\Rian\\Downloads\\CaminhoPix";

    private final PixRepository pixRepository;
    private final PixMapper pixMapper;
    private final CidadeService cidadeService;
    private final CobrancasService cobrancasService;

    public PixService(PixRepository pixRepository, PixMapper pixMapper, CidadeService cidadeService, CobrancasService cobrancasService){
        this.pixRepository = pixRepository;
        this.pixMapper = pixMapper;
        this.cidadeService = cidadeService;
        this.cobrancasService = cobrancasService;
    }

    public List<PixDTO> buscaTodos() {
        return pixRepository.findAll()
                .stream()
                .map(pixMapper::toDTO)
                .toList();
    }

    public PixDTO buscarPorId(Long id) {
        Pix pix = pixRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pix não encontrado!"));
        return pixMapper.toDTO(pix);
    }

    public List<PixDTO> buscarPorCobranca(Long cobrancaId) {
        Cobranca cobranca = cobrancasService.getById(cobrancaId);

        return pixRepository
                .findByCobranca(cobranca)
                .stream()
                .map(pixMapper::toDTO)
                .toList();
    }


    public PixDTO salvar(PixDTO pixDTO, MultipartFile arquivo) {

        Pix pix = pixMapper.toEntity(pixDTO);
        pix.setCidade(cidadeService.getById(pixDTO.getCidadeId()));

        if (pixDTO.getCobrancaId() != null) {
            pix.setCobranca(cobrancasService.getById(pixDTO.getCobrancaId()));
        } else {
            pix.setCobranca(null);
        }

        Pix salvo = pixRepository.save(pix);

        if (arquivo != null && !arquivo.isEmpty()) {
            try {
                Files.createDirectories(Paths.get(DIRETORIO_PIX));

                String nomeComprovante = UUID.randomUUID() + "_" + arquivo.getOriginalFilename();
                Path caminho = Paths.get(DIRETORIO_PIX, nomeComprovante);

                Files.write(caminho, arquivo.getBytes());

                pix.setNomeComprovante(nomeComprovante);

                salvo = pixRepository.save(pix);

            } catch (IOException e) {
                throw new RuntimeException("Erro ao salvar arquivo do Pix", e);
            }
        }

        return pixMapper.toDTO(salvo);
    }


    public PixDTO editar(PixDTO dto, MultipartFile arquivo) {
        Pix pix = pixRepository.findById(dto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Pix", "id", dto.getId()));

        pixMapper.updateEntityFromDto(dto, pix);
        pix.setCidade(cidadeService.getById(dto.getCidadeId()));

        if (dto.getCobrancaId() != null) {
            pix.setCobranca(cobrancasService.getById(dto.getCobrancaId()));
        } else {
            pix.setCobranca(null);
        }

        if (arquivo != null && !arquivo.isEmpty()) {
            try {
                if (pix.getNomeComprovante() != null) {
                    Files.deleteIfExists(Paths.get(DIRETORIO_PIX, pix.getNomeComprovante()));
                }

                String novoNome = UUID.randomUUID() + "_" + arquivo.getOriginalFilename();
                Path caminho = Paths.get(DIRETORIO_PIX, novoNome);
                Files.write(caminho, arquivo.getBytes());

                pix.setNomeComprovante(novoNome);
            } catch (IOException e) {
                throw new RuntimeException("Erro ao atualizar arquivo do Pix", e);
            }
        }

        Pix salvo = pixRepository.save(pix);
        return pixMapper.toDTO(salvo);
    }

    public List<PixDTO> buscaTodosPorFiltro(PixQueryFilters filters) {
        List<Pix> pixFiltrados = pixRepository.findAll(filters.toEspecification());

        if (pixFiltrados.isEmpty()) {
            throw new ResourceNotFoundException("Não foi possível encontrar nenhum pix com os filtros especificados!");
        }

        return pixFiltrados.stream().map(pixMapper :: toDTO).toList();

    }

    public void excluir(Long id) {
        if (!pixRepository.existsById(id)) {
            throw new EntityNotFoundException("Pix não encontrado com id " + id);
        }
        pixRepository.deleteById(id);
    }
    
}
