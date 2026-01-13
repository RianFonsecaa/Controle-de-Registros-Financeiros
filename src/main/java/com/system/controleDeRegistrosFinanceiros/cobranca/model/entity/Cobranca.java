package com.system.controleDeRegistrosFinanceiros.cobranca.model.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.system.controleDeRegistrosFinanceiros.authentication.model.User;
import com.system.controleDeRegistrosFinanceiros.cidade.model.entity.Cidade;
import com.system.controleDeRegistrosFinanceiros.cobranca.model.dto.CobrancaDTO;
import com.system.controleDeRegistrosFinanceiros.funcionario.model.entity.Funcionario;
import com.system.controleDeRegistrosFinanceiros.pix.model.dto.PixDTO;
import com.system.controleDeRegistrosFinanceiros.pix.model.entity.Pix;
import com.system.controleDeRegistrosFinanceiros.vale.model.dto.ValeDTO;
import com.system.controleDeRegistrosFinanceiros.vale.model.entity.Vale;

import com.system.controleDeRegistrosFinanceiros.veiculo.model.entity.Veiculo;
import jakarta.persistence.*;
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

    @ManyToOne(optional = false)
    @JoinColumn( nullable = false)
    private User usuarioRegistrante;

    @Column(nullable = false)
    private Double valorTotalEspecie;

    @Column(nullable = false)
    private Double valorTotalPix;

    @Column(nullable = false)
    private Double valorTotalVale;
    
    @Column(nullable = false)
    private Double valorTotal;

    @Column(nullable = false)
    private LocalDate data;


    @Column(nullable = false, length = 500)
    private String observacoes;

}