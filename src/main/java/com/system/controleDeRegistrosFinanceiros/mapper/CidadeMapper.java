package com.system.controleDeRegistrosFinanceiros.mapper;

import org.mapstruct.Mapper;

import com.system.controleDeRegistrosFinanceiros.model.dtos.CidadeDTO;
import com.system.controleDeRegistrosFinanceiros.model.entidades.Cidade;

@Mapper(componentModel = "spring")
public interface CidadeMapper {

    Cidade toEntity(CidadeDTO cidadeDto);

    CidadeDTO toDTO(Cidade cidade);
}
