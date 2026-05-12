package com.system.controleDeRegistrosFinanceiros.vale.model.dto;

import com.system.controleDeRegistrosFinanceiros.vale.model.entity.Vale;
import lombok.Data;

import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

import static com.system.controleDeRegistrosFinanceiros.vale.specifications.ValeSpec.*;

@Data
public class ValeQueryFilters {
    private String observacoes;
    private Long cobradorId;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private Double valorInicio;
    private Double valorFim;

    public Specification<Vale> toSpecification() {
        return porFuncionario(cobradorId)
                .and(observacoesContains(observacoes))
                .and(periodoEntre(dataInicio, dataFim))
                .and(valorEntre(valorInicio, valorFim));
    }
}
