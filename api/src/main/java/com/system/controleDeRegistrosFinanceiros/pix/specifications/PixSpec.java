package com.system.controleDeRegistrosFinanceiros.pix.specifications;
import com.system.controleDeRegistrosFinanceiros.pix.model.entity.Pix;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ObjectUtils;

import java.time.LocalDate;

public class PixSpec {

    public static Specification<Pix> clienteContains(String cliente){
        return (root, query, builder) -> {
            if (ObjectUtils.isEmpty(cliente)){
                return null;
            }
            return builder.like(root.get("cliente"), "%" + cliente + "%");
        };
    }

    public static Specification<Pix> porCidade(Long cidadeId) {
        return (root, query, builder) -> {
            if (ObjectUtils.isEmpty(cidadeId)){
                return null;
            }
            return builder.equal(root.get("cidade").get("id"), cidadeId);
        };
    }


    public static Specification<Pix> periodoEntre(LocalDate dataInicio, LocalDate dataFim) {
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

    public static Specification<Pix> valorEntre(Double valorInicio, Double valorFim){
        return (root, query, builder) -> {
            if (ObjectUtils.isEmpty(valorInicio) && ObjectUtils.isEmpty(valorFim)){
                return null;
            }
            if (ObjectUtils.isEmpty(valorInicio)) {
                return builder.lessThanOrEqualTo(root.get("valor"), valorFim);
            }
            if (ObjectUtils.isEmpty(valorFim)) {
                return builder.greaterThanOrEqualTo(root.get("valor"), valorInicio );
            }
            return builder.between(root.get("valor"), valorInicio, valorFim);
        };
    }
}
