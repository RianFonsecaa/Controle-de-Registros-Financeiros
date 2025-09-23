package com.system.controleDeRegistrosFinanceiros.funcionario.mapper;

import org.mapstruct.Mapper;

import com.system.controleDeRegistrosFinanceiros.funcionario.model.dto.FuncionarioDTO;
import com.system.controleDeRegistrosFinanceiros.funcionario.model.entity.Funcionario;


@Mapper(componentModel = "spring")
public interface FuncionarioMapper {

    FuncionarioDTO toDTO(Funcionario entidade);

    Funcionario toEntity(FuncionarioDTO dto);
    
}
