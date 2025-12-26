package com.system.controleDeRegistrosFinanceiros.vale.model.entity;

import java.time.LocalDate;

import com.system.controleDeRegistrosFinanceiros.cobranca.model.entity.Cobranca;

import com.system.controleDeRegistrosFinanceiros.funcionario.model.entity.Funcionario;
import jakarta.persistence.*;
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

    @Column
    private String justificativa;

    @Column
    private Double valor;

    @Column
    private LocalDate data;

    @ManyToOne(optional = true)
    @JoinColumn
    private Cobranca cobranca;

    @ManyToOne(optional = true)
    @JoinColumn
    private Funcionario funcionario;


}