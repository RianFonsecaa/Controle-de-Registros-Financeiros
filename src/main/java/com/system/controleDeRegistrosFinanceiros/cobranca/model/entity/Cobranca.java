package com.system.controleDeRegistrosFinanceiros.cobranca.model.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.system.controleDeRegistrosFinanceiros.cidade.model.entity.Cidade;
import com.system.controleDeRegistrosFinanceiros.cobranca.model.dto.CobrancaDTO;
import com.system.controleDeRegistrosFinanceiros.funcionario.model.Funcionario;
import com.system.controleDeRegistrosFinanceiros.pix.model.dto.PixDTO;
import com.system.controleDeRegistrosFinanceiros.pix.model.entity.Pix;
import com.system.controleDeRegistrosFinanceiros.vale.model.Vale;
import com.system.controleDeRegistrosFinanceiros.vale.model.ValeDTO;

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
import lombok.NoArgsConstructor;
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
    @JoinColumn(name = "cidade_id", nullable = false)
    private Cidade cidade;

    @ManyToOne(optional = false)
    @JoinColumn(name = "cobrador_id", nullable = false)
    private Funcionario cobrador;

    @Column(name = "valor_especie", nullable = false)
    private double valorEspecie;

    @Column(name = "valor_total_pix", nullable = false)
    private double valorTotalPix;

    @Column(name = "valor_total_vale", nullable = false)
    private double valorTotalVale;
    
    @Column(name = "valor_total", nullable = false)
    private double valorTotal;

    @Column(name = "data", nullable = false)
    private LocalDate data;

    @Column(name = "veiculo", length = 50)
    private String veiculo;

    @OneToMany(mappedBy = "cobranca", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Vale> vales = new ArrayList<>();

    @OneToMany(mappedBy = "cobranca", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Pix> pix = new ArrayList<>();

    @Column(name = "observacoes", length = 500)
    private String observacoes;

}