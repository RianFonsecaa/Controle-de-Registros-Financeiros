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

    public static Specification<Cobranca> porCidade(Long cidadeId) {
        return (root, query, builder) -> {
            if (ObjectUtils.isEmpty(cidadeId)){
                return null;
            }
            return builder.equal(root.get("cidade").get("id"), cidadeId);
        };
    }

    public static Specification<Cobranca> porCobrador(Long funcionarioId) {
        return (root, query, builder) -> {
            if (ObjectUtils.isEmpty(funcionarioId)){
                return null;
            }
            return builder.equal(root.get("cobrador").get("id"), funcionarioId);
        };
    }

    public static Specification<Cobranca> porRegistrante(String usuarioRegistrante) {
        return (root, query, builder) -> {
            if (ObjectUtils.isEmpty(usuarioRegistrante)){
                return null;
            }
            return builder.like(root.get("usuarioRegistranteId"), "%" + usuarioRegistrante + "%");
        };
    }

    public static Specification<Cobranca> periodoEntre(LocalDate dataInicio, LocalDate dataFim) {
        return (root, query, builder) -> {
            if (ObjectUtils.isEmpty(dataInicio) && ObjectUtils.isEmpty(dataFim)){
                return null;
            }
            if (ObjectUtils.isEmpty(dataInicio)) {
                return builder.lessThanOrEqualTo(root.get("data"), dataFim);
            }
            if (ObjectUtils.isEmpty(dataFim)) {
                return builder.greaterThanOrEqualTo(root.get("data"), dataInicio );
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
                return builder.lessThanOrEqualTo(root.get("valorTotal"), valorFim);
            }
            if (ObjectUtils.isEmpty(valorFim)) {
                return builder.greaterThanOrEqualTo(root.get("valorTotal"), valorInicio );
            }
            return builder.between(root.get("valorTotal"), valorInicio, valorFim);
        };
    }

}
