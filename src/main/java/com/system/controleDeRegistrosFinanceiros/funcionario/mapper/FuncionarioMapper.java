package com.system.controleDeRegistrosFinanceiros.funcionario.mapper;

import org.mapstruct.Mapper;

import com.system.controleDeRegistrosFinanceiros.funcionario.model.Funcionario;
import com.system.controleDeRegistrosFinanceiros.funcionario.model.FuncionarioDTO;


@Mapper(componentModel = "spring")
public interface FuncionarioMapper {

    FuncionarioDTO toDTO(Funcionario entidade);

    Funcionario toEntity(FuncionarioDTO dto);
    
}
