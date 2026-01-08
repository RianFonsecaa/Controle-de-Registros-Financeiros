package com.system.controleDeRegistrosFinanceiros.cobranca.specifications;

import com.system.controleDeRegistrosFinanceiros.cobranca.model.entity.Cobranca;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ObjectUtils;

public class CobrancaSpec {

    public static Specification<Cobranca> observacoesContains(String observacoes){
        return (root, query, builder) -> {
            if (ObjectUtils.isEmpty(observacoes)){
                return null;
            }
            return builder.like(root.get("observacoes"), "%" + observacoes + "%");
        };
    }
}
