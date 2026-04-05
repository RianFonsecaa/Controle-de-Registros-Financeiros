package com.system.controleDeRegistrosFinanceiros.cidade.mapper;

import org.mapstruct.Mapper;

import com.system.controleDeRegistrosFinanceiros.cidade.model.dto.CidadeDTO;
import com.system.controleDeRegistrosFinanceiros.cidade.model.entity.Cidade;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;


@Mapper(componentModel = "spring")
public interface CidadeMapper {

    Cidade toEntity(CidadeDTO cidadeDto);

    CidadeDTO toDTO(Cidade cidade);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(CidadeDTO dto, @MappingTarget Cidade entidade);

}
