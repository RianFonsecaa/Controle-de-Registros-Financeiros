package com.system.controleDeRegistrosFinanceiros.pix.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.system.controleDeRegistrosFinanceiros.cidade.model.entity.Cidade;
import com.system.controleDeRegistrosFinanceiros.cidade.repository.CidadeRepository;
import com.system.controleDeRegistrosFinanceiros.cobranca.model.entity.Cobranca;
import com.system.controleDeRegistrosFinanceiros.pix.mapper.PixMapper;
import com.system.controleDeRegistrosFinanceiros.pix.model.dto.PixDTO;
import com.system.controleDeRegistrosFinanceiros.pix.model.entity.Pix;
import com.system.controleDeRegistrosFinanceiros.pix.repository.PixRepository;
import com.system.controleDeRegistrosFinanceiros.pix.service.interfaces.PixService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class PixServiceImpl implements PixService {

    private final PixRepository pixRepository;
    private final PixMapper pixMapper;
    private final CidadeRepository cidadeRepository;

    public PixServiceImpl(PixRepository pixRepository, PixMapper pixMapper, CidadeRepository cidadeRepository){
        this.pixRepository = pixRepository;
        this.pixMapper = pixMapper;
        this.cidadeRepository = cidadeRepository;
    }

    @Override
    public List<PixDTO> buscaTodos() {
        return pixRepository.findAll()
                .stream()
                .map(pixMapper::toDTO)
                .toList();
    }

    @Override
    public PixDTO buscarPorId(Long id) {
        Pix pix = pixRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pix não encontrado!"));
        return pixMapper.toDTO(pix);
    }

    @Override
    public PixDTO salvar(PixDTO pixDTO) {
        Cidade cidade = cidadeRepository.findById(pixDTO.getCidade().getId()).
            orElseThrow(() -> new RuntimeException("Cidade com o ID "+pixDTO.getCidade().getId()+"não encontrada!"));

        Pix pix = pixMapper.toEntity(pixDTO);
        pix.setCidade(cidade);
        
        Pix salvo = pixRepository.save(pix);
        return pixMapper.toDTO(salvo);
    }

    @Override
    public void excluir(Long id) {
        if (!pixRepository.existsById(id)) {
            throw new EntityNotFoundException("Pix não encontrado com id " + id);
        }
        pixRepository.deleteById(id);
    }

    @Override
    public PixDTO editar(PixDTO pixDTO) {
        if (pixDTO.getId() == null){
            throw new EntityNotFoundException("Não foi possível editar. Pix com ID " + pixDTO.getId() + " não encontrada.");
        }
        return this.salvar(pixDTO);
    }
    
}
