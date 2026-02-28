package com.system.controleDeRegistrosFinanceiros.funcionario.mapper;

import com.system.controleDeRegistrosFinanceiros.cidade.model.dto.CidadeDTO;
import com.system.controleDeRegistrosFinanceiros.cidade.model.entity.Cidade;
import org.mapstruct.Mapper;

import com.system.controleDeRegistrosFinanceiros.funcionario.model.dto.FuncionarioDTO;
import com.system.controleDeRegistrosFinanceiros.funcionario.model.entity.Funcionario;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;


@Mapper(componentModel = "spring")
public interface FuncionarioMapper {

    FuncionarioDTO toDTO(Funcionario entidade);

    Funcionario toEntity(FuncionarioDTO dto);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(FuncionarioDTO dto, @MappingTarget Funcionario entidade);

}
