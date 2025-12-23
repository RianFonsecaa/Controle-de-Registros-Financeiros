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

    public void excluir(Long id){
        if (!veiculoRepository.existsById(id)){
            throw new ResourceNotFoundException("veiculo", "Id", id);
        };
        veiculoRepository.deleteById(id);
    }

    public Veiculo getById(Long id){
        return veiculoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("veiculo", "Modelo", id));
    }
}
