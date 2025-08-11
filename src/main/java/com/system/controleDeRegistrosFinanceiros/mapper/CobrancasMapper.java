package com.system.controleDeRegistrosFinanceiros.mapper;

import org.mapstruct.Mapper;

import com.system.controleDeRegistrosFinanceiros.model.dtos.CobrancaDTO;
import com.system.controleDeRegistrosFinanceiros.model.entidades.Cobranca;

@Mapper(componentModel = "spring")
public interface CobrancasMapper {

    Cobranca toEntity(CobrancaDTO dto);

    CobrancaDTO toDTO(Cobranca entity);
}