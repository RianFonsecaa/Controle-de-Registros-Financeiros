package com.system.controleDeRegistrosFinanceiros.veiculo.mapper;

import com.system.controleDeRegistrosFinanceiros.veiculo.model.dto.VeiculoDTO;
import com.system.controleDeRegistrosFinanceiros.veiculo.model.entity.Veiculo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VeiculoMapper {
    Veiculo toEntity (VeiculoDTO veiculoDTO);

    VeiculoDTO toDTO (Veiculo veiculo);
}
