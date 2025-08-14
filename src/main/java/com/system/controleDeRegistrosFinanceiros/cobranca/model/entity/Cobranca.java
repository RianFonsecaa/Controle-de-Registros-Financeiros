package com.system.controleDeRegistrosFinanceiros.cobranca.model.entity;

import java.time.LocalDate;
import java.util.List;

import com.system.controleDeRegistrosFinanceiros.cidade.model.entity.Cidade;
import com.system.controleDeRegistrosFinanceiros.funcionario.model.Funcionario;
import com.system.controleDeRegistrosFinanceiros.pix.model.Pix;
import com.system.controleDeRegistrosFinanceiros.vale.model.Vale;

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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "cobrancas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cobranca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "cidade_id", nullable = false)
    private Cidade cidade;

    @ManyToOne(optional = false)
    @JoinColumn(name = "cobrador_id", nullable = false)
    private Funcionario cobrador;

    @Column(name = "valor_especie", nullable = false)
    private double valorEspecie;

    @Column(name = "data", nullable = false)
    private LocalDate data;

    @Column(name = "veiculo", length = 50)
    private String veiculo;

    @OneToMany(mappedBy = "cobranca", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Vale> vales;

    @OneToMany(mappedBy = "cobranca", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Pix> pix;

    @Column(name = "observacoes", length = 500)
    private String observacoes;


    public double getValorTotalPix() {
        if (this.pix == null) return 0.0;
        return this.pix.stream()
                  .mapToDouble(Pix::getValor)
                  .sum();
    }

    // Soma dos valores dos Vales
    public double getValorTotalVales() {
        if (vales == null) return 0.0;
        return vales.stream()
                    .mapToDouble(Vale::getValor)
                    .sum();
    }

    // Cálculo do valor total da cobrança
    public double calcularValorTotal() {
        return valorEspecie + getValorTotalPix() - getValorTotalVales();
    }
}