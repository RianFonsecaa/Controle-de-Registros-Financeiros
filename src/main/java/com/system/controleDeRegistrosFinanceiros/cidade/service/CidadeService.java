package com.system.controleDeRegistrosFinanceiros.cidade.service;

import com.system.controleDeRegistrosFinanceiros.exceptions.ResourceAlreadyExistsException;
import com.system.controleDeRegistrosFinanceiros.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import com.system.controleDeRegistrosFinanceiros.cidade.mapper.CidadeMapper;
import com.system.controleDeRegistrosFinanceiros.cidade.model.dto.CidadeDTO;
import com.system.controleDeRegistrosFinanceiros.cidade.model.entity.Cidade;
import com.system.controleDeRegistrosFinanceiros.cidade.repository.CidadeRepository;

import jakarta.persistence.EntityNotFoundException;

import java.util.List;


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
            throw new ResourceAlreadyExistsException("Cidade", "Nome", cidadeDTO.getNome());
        }
        Cidade cidadeSalva = cidadeRepository.save(cidadeMapper.toEntity(cidadeDTO));
        return cidadeMapper.toDTO(cidadeSalva);
    }

    public List<CidadeDTO> buscaTodos (){
        return cidadeRepository.findAll().stream().map(cidadeMapper::toDTO).toList();
    }

    public void excluir(Long id){
        if (!cidadeRepository.existsById(id)){
                throw new ResourceNotFoundException("Cidade", "Id", id);
        };
        cidadeRepository.deleteById(id);
    }

    public Cidade getByNome(String nome){
        return cidadeRepository.findByNome(nome)
                .orElseThrow(() -> new ResourceNotFoundException("Cidade", "Nome", nome));
    }
}
