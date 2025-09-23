package com.system.controleDeRegistrosFinanceiros.cobranca.model.dto;

import java.time.LocalDate;
import java.util.List;

import com.system.controleDeRegistrosFinanceiros.cidade.model.entity.Cidade;
import com.system.controleDeRegistrosFinanceiros.funcionario.model.entity.Funcionario;
import com.system.controleDeRegistrosFinanceiros.pix.model.dto.PixDTO;
import com.system.controleDeRegistrosFinanceiros.vale.model.dto.ValeDTO;

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
    private Cidade cidade;
    private Funcionario cobrador;
    private Double valorEspecie;
    private Double valorTotal;
    private Double valorTotalPix;
    private Double valorTotalVale;
    private LocalDate data;
    private String veiculo;
    private List<ValeDTO> vales;
    private List<PixDTO> pix;
    private String observacoes;
}