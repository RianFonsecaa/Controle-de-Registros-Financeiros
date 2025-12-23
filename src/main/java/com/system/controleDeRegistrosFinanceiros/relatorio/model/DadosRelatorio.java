package com.system.controleDeRegistrosFinanceiros.relatorio.model;

import java.time.LocalDate;

import com.system.controleDeRegistrosFinanceiros.cobranca.model.entity.Cobranca;

import lombok.Data;

@Data 
public class DadosRelatorio {
    private Long id;
    private String nomeCidade;
    private String nomeCobrador;
    private String registroPor;
    private Double valorEspecie;
    private Double valorPix;
    private Double valorVale;
    private Double valorTotal;
    private String veiculo;
    private String observacoes;
    private LocalDate data;

}