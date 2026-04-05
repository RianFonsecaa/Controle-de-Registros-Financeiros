package com.system.controleDeRegistrosFinanceiros.relatorio.mapper;


import com.system.controleDeRegistrosFinanceiros.cobranca.model.entity.Cobranca;
import com.system.controleDeRegistrosFinanceiros.relatorio.model.DadosRelatorio;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DadosRelatorioMapper {

    @Mapping(source = "cidade.nome", target = "nomeCidade")
    @Mapping(source = "cobrador.nome", target = "nomeCobrador")
    @Mapping(source = "veiculo.modelo", target = "veiculo")
    @Mapping(source = "valorTotalPix", target = "valorPix")
    @Mapping(source = "valorTotalVale", target = "valorVale")
    @Mapping(source = "valorEspecie", target = "valorEspecie")
    @Mapping(source = "usuarioRegistrante.name", target = "usuarioRegistrante")
    DadosRelatorio toRelatorio(Cobranca entidade);
}
