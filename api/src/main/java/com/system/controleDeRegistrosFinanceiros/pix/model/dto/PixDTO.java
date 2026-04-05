package com.system.controleDeRegistrosFinanceiros.pix.model.dto;

import java.time.LocalDate;

import com.system.controleDeRegistrosFinanceiros.cidade.model.entity.Cidade;
import com.system.controleDeRegistrosFinanceiros.cobranca.model.entity.Cobranca;

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
public class PixDTO {
    private Long id;
    private String cliente;
    private Double valor;
    private LocalDate data;
    private Long cobrancaId;
    private Long cidadeId;
    private String cidadeNome;
    private String nomeComprovante;

}