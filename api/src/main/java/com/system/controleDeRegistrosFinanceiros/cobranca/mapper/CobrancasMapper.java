package com.system.controleDeRegistrosFinanceiros.cobranca.mapper;

import com.system.controleDeRegistrosFinanceiros.pix.mapper.PixMapper;
import com.system.controleDeRegistrosFinanceiros.vale.mapper.ValeMapper;
import org.mapstruct.Mapper;

import com.system.controleDeRegistrosFinanceiros.cobranca.model.dto.CobrancaDTO;
import com.system.controleDeRegistrosFinanceiros.cobranca.model.entity.Cobranca;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring", uses = { PixMapper.class, ValeMapper.class })
public interface CobrancasMapper {

    @Mapping(source = "cidade.id", target = "cidadeId")
    @Mapping(source = "cidade.nome", target = "cidadeNome")
    @Mapping(source = "cobrador.id", target = "cobradorId")
    @Mapping(source = "cobrador.nome", target = "cobradorNome")
    @Mapping(source = "veiculo.id", target = "veiculoId")
    @Mapping(source = "veiculo.modelo", target = "veiculoModelo")
    @Mapping(source = "usuarioRegistrante.name", target = "usuarioRegistranteName")
    CobrancaDTO toDTO(Cobranca entidade);

    @Mapping(target = "cidade", ignore = true)
    @Mapping(target = "cobrador", ignore = true)
    @Mapping(target = "veiculo", ignore = true)
    @Mapping(target = "usuarioRegistrante", ignore = true)
    Cobranca toEntity(CobrancaDTO dto);
}
