package com.system.controleDeRegistrosFinanceiros.mapper;

import org.mapstruct.Mapper;

import com.system.controleDeRegistrosFinanceiros.model.dtos.FuncionarioDTO;
import com.system.controleDeRegistrosFinanceiros.model.entidades.Funcionario;

@Mapper(componentModel = "spring")
public interface FuncionarioMapper {

    FuncionarioDTO toDTO(Funcionario entidade);

    Funcionario toEntity(FuncionarioDTO dto);
    
}
