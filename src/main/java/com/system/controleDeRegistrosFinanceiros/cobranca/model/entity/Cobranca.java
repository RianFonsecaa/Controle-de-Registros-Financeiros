package com.system.controleDeRegistrosFinanceiros.cobranca.model.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.system.controleDeRegistrosFinanceiros.cidade.model.entity.Cidade;
import com.system.controleDeRegistrosFinanceiros.cobranca.model.dto.CobrancaDTO;
import com.system.controleDeRegistrosFinanceiros.funcionario.model.entity.Funcionario;
import com.system.controleDeRegistrosFinanceiros.pix.model.dto.PixDTO;
import com.system.controleDeRegistrosFinanceiros.pix.model.entity.Pix;
import com.system.controleDeRegistrosFinanceiros.vale.model.dto.ValeDTO;
import com.system.controleDeRegistrosFinanceiros.vale.model.entity.Vale;

import com.system.controleDeRegistrosFinanceiros.veiculo.model.entity.Veiculo;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "cobrancas")
@Getter
@Setter
public class Cobranca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Cidade cidade;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Funcionario cobrador;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Veiculo veiculo;

    @Column(nullable = false)
    private String registroPor;

    @Column(nullable = false)
    private double valorTotalEspecie;

    @Column(nullable = false)
    private double valorTotalPix;

    @Column(nullable = false)
    private double valorTotalVale;
    
    @Column(nullable = false)
    private double valorTotal;

    @Column(nullable = false)
    private LocalDate data;

    @OneToMany(mappedBy = "cobranca", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Vale> vales = new ArrayList<>();


    @Column(nullable = false, length = 500)
    private String observacoes;
}