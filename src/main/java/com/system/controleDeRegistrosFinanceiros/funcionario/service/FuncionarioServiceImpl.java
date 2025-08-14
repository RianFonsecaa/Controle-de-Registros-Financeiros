package com.system.controleDeRegistrosFinanceiros.funcionario.service;

import org.springframework.stereotype.Service;

import com.system.controleDeRegistrosFinanceiros.funcionario.mapper.FuncionarioMapper;
import com.system.controleDeRegistrosFinanceiros.funcionario.model.Funcionario;
import com.system.controleDeRegistrosFinanceiros.funcionario.model.FuncionarioDTO;
import com.system.controleDeRegistrosFinanceiros.funcionario.repository.FuncionarioRepository;




@Service
public class FuncionarioServiceImpl implements FuncionarioService {

    private final FuncionarioRepository funcionarioRepository;

    private final FuncionarioMapper funcionarioMapper;

    public FuncionarioServiceImpl(FuncionarioRepository funcionarioRepository, FuncionarioMapper funcionarioMapper) {
        this.funcionarioRepository = funcionarioRepository;
        this.funcionarioMapper = funcionarioMapper;
    }

    @Override
    public FuncionarioDTO criar(FuncionarioDTO funcionarioDTO) {
        Funcionario funcionarioSalvo = funcionarioRepository.save(funcionarioMapper.toEntity(funcionarioDTO));
        return funcionarioMapper.toDTO(funcionarioSalvo);
    }
}
