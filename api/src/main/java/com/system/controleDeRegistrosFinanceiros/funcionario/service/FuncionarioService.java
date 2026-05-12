package com.system.controleDeRegistrosFinanceiros.funcionario.service;


import com.system.controleDeRegistrosFinanceiros.exceptions.ResourceAlreadyExistsException;
import com.system.controleDeRegistrosFinanceiros.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import com.system.controleDeRegistrosFinanceiros.funcionario.mapper.FuncionarioMapper;
import com.system.controleDeRegistrosFinanceiros.funcionario.model.dto.FuncionarioDTO;
import com.system.controleDeRegistrosFinanceiros.funcionario.model.entity.Funcionario;
import com.system.controleDeRegistrosFinanceiros.funcionario.repository.FuncionarioRepository;

import jakarta.persistence.EntityNotFoundException;

import java.util.List;

@Service
public class FuncionarioService {

    private final FuncionarioRepository funcionarioRepository;

    private final FuncionarioMapper funcionarioMapper;

    public FuncionarioService(FuncionarioRepository funcionarioRepository, FuncionarioMapper funcionarioMapper) {
        this.funcionarioRepository = funcionarioRepository;
        this.funcionarioMapper = funcionarioMapper;
    }

    public FuncionarioDTO criar(FuncionarioDTO funcionarioDTO) {
        if (funcionarioRepository.existsByNome(funcionarioDTO.getNome())) {
            throw new ResourceAlreadyExistsException("Funcionário", "Nome", funcionarioDTO.getNome());
        }
        Funcionario funcionarioSalvo = funcionarioRepository.save(funcionarioMapper.toEntity(funcionarioDTO));
        return funcionarioMapper.toDTO(funcionarioSalvo);
    }

    public List<FuncionarioDTO> buscaTodos(){
        return funcionarioRepository.findAll().stream().map(funcionarioMapper::toDTO).toList();
    }


    public Funcionario getById(Long id){
        return funcionarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Funcionario", "Nome", id));
    }


    public void toggleStatus(Long funcionarioId){
        Funcionario funcionario = funcionarioRepository.findById(funcionarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Funcionario", "ID",funcionarioId));
        funcionario.setAtivo(!funcionario.getAtivo());
        funcionarioRepository.save(funcionario);
    }

    public FuncionarioDTO editar(FuncionarioDTO funcionarioDTO) {
        Funcionario funcionario = funcionarioRepository.findById(funcionarioDTO.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Funcionario", "ID", funcionarioDTO.getId()));

        if (funcionarioRepository.existsByNomeAndIdNot(funcionarioDTO.getNome(), funcionarioDTO.getId())) {
            throw new ResourceAlreadyExistsException("Funcionario", "Nome", funcionarioDTO.getNome());
        }

        funcionarioMapper.updateEntityFromDto(funcionarioDTO, funcionario);
        return funcionarioMapper.toDTO(funcionarioRepository.save(funcionario));
    }
}
