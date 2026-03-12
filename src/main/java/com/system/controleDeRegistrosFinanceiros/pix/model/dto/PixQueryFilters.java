package com.system.controleDeRegistrosFinanceiros.pix.model.dto;

import com.system.controleDeRegistrosFinanceiros.pix.model.entity.Pix;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

import static com.system.controleDeRegistrosFinanceiros.pix.specifications.PixSpec.*;

@Data
public class PixQueryFilters {

    private String cliente;

    private Long cidadeId;

    private LocalDate dataInicio;

    private LocalDate dataFim;

    private Double valorInicio;

    private Double valorFim;

    public Specification<Pix> toEspecification(){
        return porCidade(cidadeId)
                .and(clienteContains(cliente))
                .and(periodoEntre(dataInicio,dataFim))
                .and(valorEntre(valorInicio, valorFim));
    }
}