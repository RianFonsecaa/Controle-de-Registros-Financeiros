package com.system.controleDeRegistrosFinanceiros.veiculo.mapper;

import com.system.controleDeRegistrosFinanceiros.cidade.model.dto.CidadeDTO;
import com.system.controleDeRegistrosFinanceiros.cidade.model.entity.Cidade;
import com.system.controleDeRegistrosFinanceiros.veiculo.model.dto.VeiculoDTO;
import com.system.controleDeRegistrosFinanceiros.veiculo.model.entity.Veiculo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface VeiculoMapper {
    Veiculo toEntity (VeiculoDTO veiculoDTO);

    VeiculoDTO toDTO (Veiculo veiculo);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(VeiculoDTO dto, @MappingTarget Veiculo entidade);
}
