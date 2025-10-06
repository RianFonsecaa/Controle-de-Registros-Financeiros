package com.system.controleDeRegistrosFinanceiros.relatorio.model;

import java.time.LocalDate;

import com.system.controleDeRegistrosFinanceiros.cobranca.model.entity.Cobranca;

import lombok.Data;

@Data 
public class CobrancaDiaria {
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

    public CobrancaDiaria(Cobranca cobranca) {
        this.id = cobranca.getId();
        this.nomeCidade = cobranca.getCidade().getNome();
        this.nomeCobrador = cobranca.getCobrador().getNome();
        this.registroPor = cobranca.getRegistroPor();
        this.valorEspecie = cobranca.getValorEspecie();
        this.valorPix = cobranca.getValorTotalPix();
        this.valorVale = cobranca.getValorTotalVale();
        this.valorTotal = cobranca.getValorTotal();
        this.veiculo = cobranca.getVeiculo();
        this.observacoes = cobranca.getObservacoes();
        this.data = LocalDate.now(); 
    }
}