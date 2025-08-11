package com.system.controleDeRegistrosFinanceiros.model.dtos;

import java.time.LocalDate;

import com.system.controleDeRegistrosFinanceiros.model.entidades.Cobranca;

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
public class DespesaDTO {

    private Long id;
    private String observacoes;
    private Double valor;
    private LocalDate data;

}
