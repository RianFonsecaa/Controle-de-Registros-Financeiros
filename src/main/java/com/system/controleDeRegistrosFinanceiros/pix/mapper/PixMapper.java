package com.system.controleDeRegistrosFinanceiros.pix.mapper;

import org.mapstruct.Mapper;

import com.system.controleDeRegistrosFinanceiros.pix.model.dto.PixDTO;
import com.system.controleDeRegistrosFinanceiros.pix.model.entity.Pix;



@Mapper(componentModel = "spring")
public interface PixMapper {

    Pix toEntity(PixDTO dto);

    PixDTO toDTO(Pix entidade);
}