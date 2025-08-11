package com.system.controleDeRegistrosFinanceiros.services;

import org.springframework.stereotype.Service;

import com.system.controleDeRegistrosFinanceiros.mapper.CidadeMapper;
import com.system.controleDeRegistrosFinanceiros.model.dtos.CidadeDTO;
import com.system.controleDeRegistrosFinanceiros.model.entidades.Cidade;
import com.system.controleDeRegistrosFinanceiros.repository.CidadeRepository;
import com.system.controleDeRegistrosFinanceiros.services.interfaces.CidadeService;

@Service
public class CidadeServiceImpl implements CidadeService {

    private final CidadeRepository cidadeRepository;

    private final CidadeMapper cidadeMapper;

    public CidadeServiceImpl(CidadeRepository cidadeRepository, CidadeMapper cidadeMapper) {
        this.cidadeRepository = cidadeRepository;
        this.cidadeMapper = cidadeMapper;
    }

    @Override
    public CidadeDTO criar(CidadeDTO cidadeDTO) {
        Cidade cidadeSalva = cidadeRepository.save(cidadeMapper.toEntity(cidadeDTO));
        return cidadeMapper.toDTO(cidadeSalva);
    }
}
