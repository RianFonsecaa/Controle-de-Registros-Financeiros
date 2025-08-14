package com.system.controleDeRegistrosFinanceiros.pix.mapper;

import org.mapstruct.Mapper;

import com.system.controleDeRegistrosFinanceiros.pix.model.Pix;
import com.system.controleDeRegistrosFinanceiros.pix.model.PixDTO;



@Mapper(componentModel = "spring")
public interface PixMapper {

    Pix toEntity(PixDTO dto);

    PixDTO toDTO(Pix entidade);
}