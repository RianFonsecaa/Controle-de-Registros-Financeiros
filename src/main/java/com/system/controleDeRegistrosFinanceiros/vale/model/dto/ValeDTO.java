package com.system.controleDeRegistrosFinanceiros.vale.model.dto;

import java.time.LocalDate;

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
public class ValeDTO {

    private Long id;
    private String observacoes;
    private Double valor;
    private LocalDate data;

}