package com.system.controleDeRegistrosFinanceiros.pix.service.interfaces;

import java.util.List;

import com.system.controleDeRegistrosFinanceiros.pix.model.dto.PixDTO;

public interface PixService {

    List<PixDTO> buscaTodos();

    PixDTO buscarPorId(Long id);

    PixDTO salvar(PixDTO pixDTO);

    void excluir(Long id);

    PixDTO editar(PixDTO pixDTO);

}
