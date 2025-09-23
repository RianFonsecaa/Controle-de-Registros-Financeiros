package com.system.controleDeRegistrosFinanceiros.funcionario.service;

import org.springframework.stereotype.Service;

import com.system.controleDeRegistrosFinanceiros.funcionario.mapper.FuncionarioMapper;
import com.system.controleDeRegistrosFinanceiros.funcionario.model.dto.FuncionarioDTO;
import com.system.controleDeRegistrosFinanceiros.funcionario.model.entity.Funcionario;
import com.system.controleDeRegistrosFinanceiros.funcionario.repository.FuncionarioRepository;

import jakarta.persistence.EntityNotFoundException;




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
             throw new IllegalArgumentException("Funcionário já cadastrado.");
        }
        Funcionario funcionarioSalvo = funcionarioRepository.save(funcionarioMapper.toEntity(funcionarioDTO));
        return funcionarioMapper.toDTO(funcionarioSalvo);
    }

    public void excluir(Long id){
        if (!funcionarioRepository.existsById(id)){
            throw new EntityNotFoundException("Não foi possível excluir. Funcionário com ID " + id + " não encontrado."); 
        } 
        funcionarioRepository.deleteById(id);
    }
}
