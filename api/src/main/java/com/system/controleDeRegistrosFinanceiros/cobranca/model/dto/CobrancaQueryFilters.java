package com.system.controleDeRegistrosFinanceiros.cobranca.model.dto;

import com.system.controleDeRegistrosFinanceiros.cobranca.model.entity.Cobranca;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

import static com.system.controleDeRegistrosFinanceiros.cobranca.specifications.CobrancaSpec.*;

@Data
public class CobrancaQueryFilters {

    private String observacoes;

    private Long cidadeId;

    private Long cobradorId;

    private String registranteLogin;

    private LocalDate dataInicio;

    private LocalDate dataFim;

    private Double valorInicio;

    private Double valorFim;

    public Specification<Cobranca> toEspecification(){
        return observacoesContains(observacoes)
                .and(porCidade(cidadeId))
                .and(porCobrador(cobradorId))
                .and(porRegistrante(registranteLogin))
                .and(periodoEntre(dataInicio,dataFim))
                .and(valorTotalEntre(valorInicio, valorFim));
    }
}
