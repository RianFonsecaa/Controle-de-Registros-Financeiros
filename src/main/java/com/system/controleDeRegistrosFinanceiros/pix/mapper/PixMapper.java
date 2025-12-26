package com.system.controleDeRegistrosFinanceiros.pix.mapper;

import org.mapstruct.Mapper;

import com.system.controleDeRegistrosFinanceiros.pix.model.dto.PixDTO;
import com.system.controleDeRegistrosFinanceiros.pix.model.entity.Pix;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface PixMapper {

    @Mapping(target = "cidade", ignore = true)
    @Mapping(target = "cobranca", ignore = true)
    @Mapping(target = "caminhoArquivo", ignore = true)
    @Mapping(target = "nomeArquivo", ignore = true)
    @Mapping(target = "tipoArquivo", ignore = true)
    Pix toEntity(PixDTO dto);

    @Mapping(source = "cidade.id", target = "cidadeId")
    @Mapping(source = "cidade.nome", target = "cidadeNome")
    @Mapping(source = "cobranca.id", target = "cobrancaId")
    PixDTO toDTO(Pix entity);
}