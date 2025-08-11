package com.system.controleDeRegistrosFinanceiros.mapper;

import org.mapstruct.Mapper;

import com.system.controleDeRegistrosFinanceiros.model.dtos.PixDTO;
import com.system.controleDeRegistrosFinanceiros.model.entidades.Cobranca;
import com.system.controleDeRegistrosFinanceiros.model.entidades.Pix;

@Mapper(componentModel = "spring")
public interface PixMapper {

    Pix toEntity(PixDTO dto);

    PixDTO toDTO(Pix entidade);
}