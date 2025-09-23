package com.system.controleDeRegistrosFinanceiros.cidade.service;

import org.springframework.stereotype.Service;

import com.system.controleDeRegistrosFinanceiros.cidade.mapper.CidadeMapper;
import com.system.controleDeRegistrosFinanceiros.cidade.model.dto.CidadeDTO;
import com.system.controleDeRegistrosFinanceiros.cidade.model.entity.Cidade;
import com.system.controleDeRegistrosFinanceiros.cidade.repository.CidadeRepository;

import jakarta.persistence.EntityNotFoundException;


@Service
public class CidadeService {

    private final CidadeRepository cidadeRepository;

    private final CidadeMapper cidadeMapper;

    public CidadeService(CidadeRepository cidadeRepository, CidadeMapper cidadeMapper) {
        this.cidadeRepository = cidadeRepository;
        this.cidadeMapper = cidadeMapper;
    }

    public CidadeDTO criar(CidadeDTO cidadeDTO) {
        if (cidadeRepository.existsByNome(cidadeDTO.getNome())) {
            throw new IllegalArgumentException("Cidade já cadastrada.");
        }
        Cidade cidadeSalva = cidadeRepository.save(cidadeMapper.toEntity(cidadeDTO));
        return cidadeMapper.toDTO(cidadeSalva);
    }

    public void excluir(Long id){
        if (!cidadeRepository.existsById(id)){
            throw new EntityNotFoundException("Não foi possível excluir. Cidade com ID " + id + " não encontrada.");
        };
        cidadeRepository.deleteById(id);
    }
}
