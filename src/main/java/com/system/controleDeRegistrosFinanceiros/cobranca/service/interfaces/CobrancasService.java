package com.system.controleDeRegistrosFinanceiros.cobranca.service.interfaces;

import java.util.List;

import com.system.controleDeRegistrosFinanceiros.cobranca.model.dto.CobrancaDTO;


public interface CobrancasService {

    List<CobrancaDTO> getTodos();

    CobrancaDTO buscarPorId(Long id);

    CobrancaDTO salvar(CobrancaDTO cobranca);

}
