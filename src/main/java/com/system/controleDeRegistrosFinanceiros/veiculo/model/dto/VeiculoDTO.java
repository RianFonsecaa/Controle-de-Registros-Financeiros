package com.system.controleDeRegistrosFinanceiros.veiculo.model.dto;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VeiculoDTO {

    private Long id;

    private String modelo;

    private String placa;

    private Boolean ativo;

}
