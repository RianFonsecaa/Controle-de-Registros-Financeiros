package com.system.controleDeRegistrosFinanceiros.vale.service;

import com.system.controleDeRegistrosFinanceiros.cidade.service.CidadeService;
import com.system.controleDeRegistrosFinanceiros.funcionario.service.FuncionarioService;
import com.system.controleDeRegistrosFinanceiros.vale.mapper.ValeMapper;
import com.system.controleDeRegistrosFinanceiros.vale.model.dto.ValeDTO;
import com.system.controleDeRegistrosFinanceiros.vale.model.entity.Vale;
import com.system.controleDeRegistrosFinanceiros.vale.repository.ValeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ValeService{

    private final ValeRepository valeRepository;
    private final ValeMapper valeMapper;
    private final FuncionarioService funcionarioService;

    public ValeService(ValeRepository valeRepository, ValeMapper valeMapper, FuncionarioService funcionarioService){
        this.valeRepository = valeRepository;
        this.valeMapper = valeMapper;
        this.funcionarioService = funcionarioService;
    }

    public List<ValeDTO> buscaTodos() {
        return valeRepository.findAll()
                .stream()
                .map(valeMapper::toDTO)
                .toList();
    }

    public ValeDTO buscarPorId(Long id) {
        Vale vale = valeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vale não encontrado!"));
        return valeMapper.toDTO(vale);
    }

    public ValeDTO salvar(ValeDTO valeDTO) {
        Vale vale = valeMapper.toEntity(valeDTO);
        vale.setFuncionario(funcionarioService.getById(valeDTO.getFuncionarioId()));
        Vale salvo = valeRepository.save(vale);
        return valeMapper.toDTO(salvo);
    }

    public void excluir(Long id) {
        if (!valeRepository.existsById(id)) {
            throw new EntityNotFoundException("Vale não encontrado com id " + id);
        }
        valeRepository.deleteById(id);
    }

    public ValeDTO editar(ValeDTO valeDTO) {
        if (valeDTO.getId() == null){
            throw new EntityNotFoundException("Não foi possível editar. Vale com ID " + valeDTO.getId() + " não encontrada.");
        }
        return this.salvar(valeDTO);
    }

}