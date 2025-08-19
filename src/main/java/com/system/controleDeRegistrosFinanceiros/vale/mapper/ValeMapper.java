package com.system.controleDeRegistrosFinanceiros.vale.mapper;

import org.mapstruct.Mapper;

import com.system.controleDeRegistrosFinanceiros.vale.model.Vale;
import com.system.controleDeRegistrosFinanceiros.vale.model.ValeDTO;

@Mapper(componentModel = "spring")
public interface ValeMapper {

    Vale toEntity(ValeDTO dto);

    ValeDTO toDTO(Vale entity);
    
}
