package com.system.controleDeRegistrosFinanceiros.cidade.mapper;

import org.mapstruct.Mapper;

import com.system.controleDeRegistrosFinanceiros.cidade.model.dto.CidadeDTO;
import com.system.controleDeRegistrosFinanceiros.cidade.model.entity.Cidade;


@Mapper(componentModel = "spring")
public interface CidadeMapper {

    Cidade toEntity(CidadeDTO cidadeDto);

    CidadeDTO toDTO(Cidade cidade);
}
