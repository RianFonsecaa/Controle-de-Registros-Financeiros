package com.system.controleDeRegistrosFinanceiros.veiculo.service;


import com.system.controleDeRegistrosFinanceiros.exceptions.ResourceAlreadyExistsException;
import com.system.controleDeRegistrosFinanceiros.exceptions.ResourceNotFoundException;
import com.system.controleDeRegistrosFinanceiros.veiculo.mapper.VeiculoMapper;
import com.system.controleDeRegistrosFinanceiros.veiculo.model.dto.VeiculoDTO;
import com.system.controleDeRegistrosFinanceiros.veiculo.model.entity.Veiculo;
import com.system.controleDeRegistrosFinanceiros.veiculo.repository.VeiculoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VeiculoService {

    private final VeiculoRepository veiculoRepository;

    private final VeiculoMapper veiculoMapper;

    public VeiculoService(VeiculoRepository veiculoRepository, VeiculoMapper veiculoMapper) {
        this.veiculoRepository = veiculoRepository;
        this.veiculoMapper = veiculoMapper;
    }

    public VeiculoDTO criar(VeiculoDTO veiculoDTO) {
        if (veiculoRepository.existsByPlaca(veiculoDTO.getPlaca())) {
            throw new ResourceAlreadyExistsException("veiculo", "Placa", veiculoDTO.getPlaca());
        }
        Veiculo veiculosalva = veiculoRepository.save(veiculoMapper.toEntity(veiculoDTO));
        return veiculoMapper.toDTO(veiculosalva);
    }

    public List<VeiculoDTO> buscaTodos (){
        return veiculoRepository.findAll().stream().map(veiculoMapper::toDTO).toList();
    }

    public Veiculo getById(Long id){
        return veiculoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("veiculo", "Modelo", id));
    }

    public void toggleStatus(Long veiculoId){
        Veiculo veiculo = veiculoRepository.findById(veiculoId)
                .orElseThrow(() -> new ResourceNotFoundException("Veiculo", "ID",veiculoId));
        veiculo.setAtivo(!veiculo.getAtivo());
        veiculoRepository.save(veiculo);
    }

    public VeiculoDTO editar(VeiculoDTO veiculoDTO) {
        Veiculo veiculo = veiculoRepository.findById(veiculoDTO.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Veiculo", "ID", veiculoDTO.getId()));

        if (veiculoRepository.existsByPlacaAndIdNot(veiculoDTO.getPlaca(), veiculoDTO.getId())) {
            throw new ResourceAlreadyExistsException("Veiculo", "Placa", veiculoDTO.getPlaca());
        }

        veiculoMapper.updateEntityFromDto(veiculoDTO, veiculo);
        return veiculoMapper.toDTO(veiculoRepository.save(veiculo));
    }
}
