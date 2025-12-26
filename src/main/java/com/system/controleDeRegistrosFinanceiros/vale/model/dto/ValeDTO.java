package com.system.controleDeRegistrosFinanceiros.vale.model.dto;

import java.time.LocalDate;

import com.system.controleDeRegistrosFinanceiros.funcionario.model.entity.Funcionario;
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
    private Long funcionarioId;
    private String funcionarioNome;
    private String justificativa;
    private Double valor;
    private LocalDate data;

}