package com.system.controleDeRegistrosFinanceiros.cidade.service;

import com.system.controleDeRegistrosFinanceiros.cobranca.model.dto.CobrancaDTO;
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
        Cidade cidade = cidadeMapper.toEntity(cidadeDTO);
        cidade.setAtivo(true);
        return cidadeMapper.toDTO(cidadeRepository.save(cidade));
    }

    public List<CidadeDTO> buscaTodos (){
        return cidadeRepository.findAll().stream().map(cidadeMapper::toDTO).toList();
    }

    public Cidade getById(Long id){
        return cidadeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cidade", "Nome", id));
    }

    public void toggleStatus(Long cidadeId){
        Cidade cidade = cidadeRepository.findById(cidadeId)
                .orElseThrow(() -> new ResourceNotFoundException("Cidade", "ID",cidadeId));
        cidade.setAtivo(!cidade.getAtivo());
        cidadeRepository.save(cidade);
    }

    public CidadeDTO editar(CidadeDTO cidadeDTO) {
        Cidade cidade = cidadeRepository.findById(cidadeDTO.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Cidade", "ID", cidadeDTO.getId()));

        if (cidadeRepository.existsByNomeAndIdNot(cidadeDTO.getNome(), cidadeDTO.getId())) {
            throw new ResourceAlreadyExistsException("Cidade", "Nome", cidadeDTO.getNome());
        }

        cidadeMapper.updateEntityFromDto(cidadeDTO, cidade);
        return cidadeMapper.toDTO(cidadeRepository.save(cidade));
    }
}
