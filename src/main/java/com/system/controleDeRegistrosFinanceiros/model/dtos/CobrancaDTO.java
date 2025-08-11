package com.system.controleDeRegistrosFinanceiros.model.dtos;

import java.time.LocalDate;
import java.util.List;

import com.system.controleDeRegistrosFinanceiros.model.entidades.Cidade;
import com.system.controleDeRegistrosFinanceiros.model.entidades.Despesa;
import com.system.controleDeRegistrosFinanceiros.model.entidades.Funcionario;
import com.system.controleDeRegistrosFinanceiros.model.entidades.Vale;

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
    private Boolean entregue;
    private LocalDate data;
    private String veiculo;
    private List<ValeDTO> vales;
    private List<DespesaDTO> despesas;
    private List<PixDTO> pix;
    private String observacoes;
}