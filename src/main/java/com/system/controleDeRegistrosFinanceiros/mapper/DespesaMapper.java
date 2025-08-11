package com.system.controleDeRegistrosFinanceiros.mapper;

import org.mapstruct.Mapper;

import com.system.controleDeRegistrosFinanceiros.model.dtos.DespesaDTO;
import com.system.controleDeRegistrosFinanceiros.model.dtos.ValeDTO;
import com.system.controleDeRegistrosFinanceiros.model.entidades.Despesa;
import com.system.controleDeRegistrosFinanceiros.model.entidades.Vale;

@Mapper(componentModel = "spring")
public interface DespesaMapper {

    Despesa toEntity(DespesaDTO dto);

    DespesaDTO toDTO(Despesa entidade);

}