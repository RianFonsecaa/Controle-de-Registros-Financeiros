package com.system.controleDeRegistrosFinanceiros.vale.model;

import java.time.LocalDate;

import com.system.controleDeRegistrosFinanceiros.cobranca.model.entity.Cobranca;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "vales")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "funcionario", nullable = false, length = 100)
    private String funcionario;

    @Column(name = "observacoes", nullable = false)
    private String observacoes;

    @Column(name = "valor", nullable = false)
    private Double valor;

    @Column(name = "data", nullable = false)
    private LocalDate data;

    @ManyToOne(optional = true)
    @JoinColumn(name = "cobranca_id", nullable = true)
    private Cobranca cobranca;
}