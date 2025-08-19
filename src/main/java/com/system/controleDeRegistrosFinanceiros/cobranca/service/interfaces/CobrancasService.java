package com.system.controleDeRegistrosFinanceiros.cobranca.service.interfaces;

import java.util.List;

import com.system.controleDeRegistrosFinanceiros.cobranca.model.dto.CobrancaDTO;


public interface CobrancasService {

    List<CobrancaDTO> buscaTodos();

    CobrancaDTO buscarPorId(Long id);

    CobrancaDTO salvar(CobrancaDTO cobrancaDto);

    void excluir(Long id);

    CobrancaDTO editar(CobrancaDTO cobrancaDTO);

}
