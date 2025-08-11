package com.system.controleDeRegistrosFinanceiros.services;

import org.springframework.stereotype.Service;

import com.system.controleDeRegistrosFinanceiros.mapper.FuncionarioMapper;
import com.system.controleDeRegistrosFinanceiros.model.dtos.FuncionarioDTO;
import com.system.controleDeRegistrosFinanceiros.model.entidades.Funcionario;
import com.system.controleDeRegistrosFinanceiros.repository.FuncionarioRepository;
import com.system.controleDeRegistrosFinanceiros.services.interfaces.FuncionarioService;

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
