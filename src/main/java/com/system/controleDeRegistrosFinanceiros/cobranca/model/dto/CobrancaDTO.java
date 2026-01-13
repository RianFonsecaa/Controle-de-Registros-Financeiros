package com.system.controleDeRegistrosFinanceiros.cobranca.model.dto;

import java.time.LocalDate;
import java.util.List;

import com.system.controleDeRegistrosFinanceiros.cidade.model.entity.Cidade;
import com.system.controleDeRegistrosFinanceiros.funcionario.model.entity.Funcionario;
import com.system.controleDeRegistrosFinanceiros.pix.model.dto.PixDTO;
import com.system.controleDeRegistrosFinanceiros.vale.model.dto.ValeDTO;

import com.system.controleDeRegistrosFinanceiros.veiculo.model.entity.Veiculo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CobrancaDTO {
    private Long id;
    private Long cidadeId;
    private String cidadeNome;
    private Long cobradorId;
    private String cobradorNome;
    private Long veiculoId;
    private String veiculoModelo;
    private String usuarioRegistranteId;
    private String usuarioRegistranteName;
    private Double valorTotalEspecie;
    private Double valorTotal;
    private Double valorTotalPix;
    private Double valorTotalVale;
    private LocalDate data;
    private String observacoes;
}