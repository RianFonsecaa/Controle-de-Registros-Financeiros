package com.system.controleDeRegistrosFinanceiros.cobranca.model.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

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
    private String usuarioRegistranteName;
    private Double valorEspecie;
    private Double valorTotal;
    private Double valorTotalPix;
    private Double valorTotalVale;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate data;
    private String observacoes;
}
