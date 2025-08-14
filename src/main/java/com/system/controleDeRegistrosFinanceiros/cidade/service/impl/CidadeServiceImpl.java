package com.system.controleDeRegistrosFinanceiros.cidade.service.impl;

import org.springframework.stereotype.Service;

import com.system.controleDeRegistrosFinanceiros.cidade.mapper.CidadeMapper;
import com.system.controleDeRegistrosFinanceiros.cidade.model.dto.CidadeDTO;
import com.system.controleDeRegistrosFinanceiros.cidade.model.entity.Cidade;
import com.system.controleDeRegistrosFinanceiros.cidade.repository.CidadeRepository;
import com.system.controleDeRegistrosFinanceiros.cidade.service.interfaces.CidadeService;


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
