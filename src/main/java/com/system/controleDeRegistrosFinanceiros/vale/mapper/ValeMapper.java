package com.system.controleDeRegistrosFinanceiros.vale.mapper;

import org.mapstruct.Mapper;

import com.system.controleDeRegistrosFinanceiros.vale.model.dto.ValeDTO;
import com.system.controleDeRegistrosFinanceiros.vale.model.entity.Vale;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ValeMapper {

    @Mapping(target = "funcionario", ignore = true)
    @Mapping(target = "cobranca", ignore = true)
    Vale toEntity(ValeDTO dto);

    @Mapping(source = "funcionario.id", target = "funcionarioId")
    @Mapping(source = "funcionario.nome", target = "funcionarioNome")
    @Mapping(source = "cobranca.id", target = "cobrancaId")
    ValeDTO toDTO(Vale entidade);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(ValeDTO dto, @MappingTarget Vale entity);
}
