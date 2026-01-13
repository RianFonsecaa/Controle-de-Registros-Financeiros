package com.system.controleDeRegistrosFinanceiros.cobranca.specifications;

import com.system.controleDeRegistrosFinanceiros.cobranca.model.entity.Cobranca;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ObjectUtils;

import java.time.LocalDate;

public class CobrancaSpec {

    public static Specification<Cobranca> observacoesContains(String observacoes){
        return (root, query, builder) -> {
            if (ObjectUtils.isEmpty(observacoes)){
                return null;
            }
            return builder.like(root.get("observacoes"), "%" + observacoes + "%");
        };
    }

    public static Specification<Cobranca> porCidade(Double cidadeId) {
        return (root, query, builder) -> {
            if (ObjectUtils.isEmpty(cidadeId)){
                return null;
            }
            return builder.equal(root.get("cidade").get("id"), cidadeId);
        };
    }

    public static Specification<Cobranca> porCobrador(Double funcionarioId) {
        return (root, query, builder) -> {
            if (ObjectUtils.isEmpty(funcionarioId)){
                return null;
            }
            return builder.equal(root.get("cobrador").get("id"), funcionarioId);
        };
    }

    public static Specification<Cobranca> registradoPor(String usuarioRegistranteId) {
        return (root, query, builder) -> {
            if (ObjectUtils.isEmpty(usuarioRegistranteId)){
                return null;
            }
            return builder.equal(root.get("usuarioRegistranteId"), usuarioRegistranteId);
        };
    }

    public static Specification<Cobranca> periodoEntre(LocalDate dataInicio, LocalDate dataFim) {
        return (root, query, builder) -> {
            if (ObjectUtils.isEmpty(dataInicio) && ObjectUtils.isEmpty(dataFim)){
                return null;
            }
            if (ObjectUtils.isEmpty(dataInicio)) {
                return builder.greaterThanOrEqualTo(root.get("data"), dataInicio );
            }
            if (ObjectUtils.isEmpty(dataFim)) {
                return builder.lessThanOrEqualTo(root.get("data"), dataFim);
            }
            return builder.between(root.get("data"), dataInicio, dataFim);
        };
    }

    public static Specification<Cobranca> valorTotalEntre(Double valorInicio, Double valorFim){
        return (root, query, builder) -> {
            if (ObjectUtils.isEmpty(valorInicio) && ObjectUtils.isEmpty(valorFim)){
                return null;
            }
            if (ObjectUtils.isEmpty(valorInicio)) {
                return builder.greaterThanOrEqualTo(root.get("valorTotal"), valorInicio );
            }
            if (ObjectUtils.isEmpty(valorFim)) {
                return builder.lessThanOrEqualTo(root.get("valorTotal"), valorFim);
            }
            return builder.between(root.get("valorTotal"), valorInicio, valorFim);
        };
    }

}
