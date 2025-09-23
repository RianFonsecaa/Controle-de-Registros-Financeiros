package com.system.controleDeRegistrosFinanceiros.vale.mapper;

import org.mapstruct.Mapper;

import com.system.controleDeRegistrosFinanceiros.vale.model.dto.ValeDTO;
import com.system.controleDeRegistrosFinanceiros.vale.model.entity.Vale;

@Mapper(componentModel = "spring")
public interface ValeMapper {

    Vale toEntity(ValeDTO dto);

    ValeDTO toDTO(Vale entity);
    
}
