package com.system.controleDeRegistrosFinanceiros.mapper;

import org.mapstruct.Mapper;

import com.system.controleDeRegistrosFinanceiros.model.dtos.ValeDTO;
import com.system.controleDeRegistrosFinanceiros.model.entidades.Vale;

@Mapper(componentModel = "spring")
public interface ValeMapper {

    ValeDTO toDTO(Vale entidade);

    Vale toEntity(ValeDTO dto);
    
}
