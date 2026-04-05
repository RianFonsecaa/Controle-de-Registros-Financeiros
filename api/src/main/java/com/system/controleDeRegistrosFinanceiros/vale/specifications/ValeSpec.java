package com.system.controleDeRegistrosFinanceiros.vale.specifications;

import com.system.controleDeRegistrosFinanceiros.vale.model.entity.Vale;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ObjectUtils;

import java.time.LocalDate;

public class ValeSpec {
    public static Specification<Vale> observacoesContains(String observacoes) {
        return (root, query, builder) -> {
            if (ObjectUtils.isEmpty(observacoes)) {
                return null;
            }
            return builder.like(builder.lower(root.get("justificativa")), "%" + observacoes.toLowerCase() + "%");
        };
    }

    public static Specification<Vale> porFuncionario(Long funcionarioId) {
        return (root, query, builder) -> {
            if (ObjectUtils.isEmpty(funcionarioId)) {
                return null;
            }
            return builder.equal(root.get("funcionario").get("id"), funcionarioId);
        };
    }

    public static Specification<Vale> periodoEntre(LocalDate dataInicio, LocalDate dataFim) {
        return (root, query, builder) -> {
            if (ObjectUtils.isEmpty(dataInicio) && ObjectUtils.isEmpty(dataFim)) {
                return null;
            }
            if (ObjectUtils.isEmpty(dataInicio)) {
                return builder.lessThanOrEqualTo(root.get("data"), dataFim);
            }
            if (ObjectUtils.isEmpty(dataFim)) {
                return builder.greaterThanOrEqualTo(root.get("data"), dataInicio);
            }
            return builder.between(root.get("data"), dataInicio, dataFim);
        };
    }

    public static Specification<Vale> valorEntre(Double valorInicio, Double valorFim) {
        return (root, query, builder) -> {
            if (ObjectUtils.isEmpty(valorInicio) && ObjectUtils.isEmpty(valorFim)) {
                return null;
            }
            if (ObjectUtils.isEmpty(valorInicio)) {
                return builder.lessThanOrEqualTo(root.get("valor"), valorFim);
            }
            if (ObjectUtils.isEmpty(valorFim)) {
                return builder.greaterThanOrEqualTo(root.get("valor"), valorInicio);
            }
            return builder.between(root.get("valor"), valorInicio, valorFim);
        };
    }
}
