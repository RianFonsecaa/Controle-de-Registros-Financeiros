package com.system.controleDeRegistrosFinanceiros.cobranca.mapper;

import org.mapstruct.Mapper;

import com.system.controleDeRegistrosFinanceiros.cobranca.model.dto.CobrancaDTO;
import com.system.controleDeRegistrosFinanceiros.cobranca.model.entity.Cobranca;


@Mapper(componentModel = "spring")
public interface CobrancasMapper {

    Cobranca toEntity(CobrancaDTO dto);

    CobrancaDTO toDTO(Cobranca entity);
}