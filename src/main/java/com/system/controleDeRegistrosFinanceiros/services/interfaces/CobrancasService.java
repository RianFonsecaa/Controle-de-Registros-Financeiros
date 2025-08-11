package com.system.controleDeRegistrosFinanceiros.services.interfaces;

import java.util.List;

import com.system.controleDeRegistrosFinanceiros.model.dtos.CobrancaDTO;

public interface CobrancasService {

    List<CobrancaDTO> getTodos();

    CobrancaDTO buscarPorId(Long id);

    CobrancaDTO salvar(CobrancaDTO cobranca);

    CobrancaDTO atualizar(Long id, CobrancaDTO cobranca);

    void deletar(Long id);
}
