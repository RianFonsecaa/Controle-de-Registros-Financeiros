package com.system.controleDeRegistrosFinanceiros.pix.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.system.controleDeRegistrosFinanceiros.cidade.model.entity.Cidade;
import com.system.controleDeRegistrosFinanceiros.cidade.repository.CidadeRepository;
import com.system.controleDeRegistrosFinanceiros.cobranca.model.entity.Cobranca;
import com.system.controleDeRegistrosFinanceiros.pix.mapper.PixMapper;
import com.system.controleDeRegistrosFinanceiros.pix.model.dto.PixDTO;
import com.system.controleDeRegistrosFinanceiros.pix.model.entity.Pix;
import com.system.controleDeRegistrosFinanceiros.pix.repository.PixRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class PixService{

    private final PixRepository pixRepository;
    private final PixMapper pixMapper;

    public PixService(PixRepository pixRepository, PixMapper pixMapper){
        this.pixRepository = pixRepository;
        this.pixMapper = pixMapper;
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

    public PixDTO salvar(PixDTO pixDTO) {
        Pix pix = pixMapper.toEntity(pixDTO);
        Pix salvo = pixRepository.save(pix);
        return pixMapper.toDTO(salvo);
    }

    public void excluir(Long id) {
        if (!pixRepository.existsById(id)) {
            throw new EntityNotFoundException("Pix não encontrado com id " + id);
        }
        pixRepository.deleteById(id);
    }

    public PixDTO editar(PixDTO pixDTO) {
        if (pixDTO.getId() == null){
            throw new EntityNotFoundException("Não foi possível editar. Pix com ID " + pixDTO.getId() + " não encontrada.");
        }
        return this.salvar(pixDTO);
    }
    
}
