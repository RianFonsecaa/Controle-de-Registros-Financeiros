package com.system.controleDeRegistrosFinanceiros.pix.model.entity;

import java.time.LocalDate;

import com.system.controleDeRegistrosFinanceiros.cidade.model.entity.Cidade;
import com.system.controleDeRegistrosFinanceiros.cobranca.model.entity.Cobranca;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "pix")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pix {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String cliente;

    @Column(nullable = false)
    private Double valor;

    @Column(nullable = false)
    private LocalDate data;

    @ManyToOne(optional = true)
    @JoinColumn(nullable = true)
    private Cobranca cobranca;

    @Column(nullable = false)
    private String cidade;
}