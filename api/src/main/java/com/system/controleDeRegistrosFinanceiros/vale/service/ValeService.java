package com.system.controleDeRegistrosFinanceiros.vale.service;

import com.system.controleDeRegistrosFinanceiros.cidade.service.CidadeService;
import com.system.controleDeRegistrosFinanceiros.cobranca.model.entity.Cobranca;
import com.system.controleDeRegistrosFinanceiros.cobranca.service.CobrancasService;
import com.system.controleDeRegistrosFinanceiros.exceptions.ResourceNotFoundException;
import com.system.controleDeRegistrosFinanceiros.funcionario.service.FuncionarioService;
import com.system.controleDeRegistrosFinanceiros.pix.model.dto.PixDTO;
import com.system.controleDeRegistrosFinanceiros.pix.model.dto.PixQueryFilters;
import com.system.controleDeRegistrosFinanceiros.pix.model.entity.Pix;
import com.system.controleDeRegistrosFinanceiros.vale.mapper.ValeMapper;
import com.system.controleDeRegistrosFinanceiros.vale.model.dto.ValeDTO;
import com.system.controleDeRegistrosFinanceiros.vale.model.dto.ValeQueryFilters;
import com.system.controleDeRegistrosFinanceiros.vale.model.entity.Vale;
import com.system.controleDeRegistrosFinanceiros.vale.repository.ValeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ValeService{

    private final ValeRepository valeRepository;
    private final ValeMapper valeMapper;
    private final FuncionarioService funcionarioService;
    private final CobrancasService cobrancasService;

    public ValeService(ValeRepository valeRepository, ValeMapper valeMapper, FuncionarioService funcionarioService, CobrancasService cobrancasService){
        this.valeRepository = valeRepository;
        this.valeMapper = valeMapper;
        this.funcionarioService = funcionarioService;
        this.cobrancasService = cobrancasService;
    }

    public List<ValeDTO> buscaTodos() {
        return valeRepository.findAll()
                .stream()
                .map(valeMapper::toDTO)
                .toList();
    }

    public ValeDTO buscarPorId(Long id) {
        Vale vale = valeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vale não encontrado!"));
        return valeMapper.toDTO(vale);
    }

    public ValeDTO salvar(ValeDTO valeDTO) {
        Vale vale = valeMapper.toEntity(valeDTO);
        vale.setFuncionario(funcionarioService.getById(valeDTO.getFuncionarioId()));

        if (valeDTO.getCobrancaId() != null) {
            vale.setCobranca(cobrancasService.getById(valeDTO.getCobrancaId()));
        } else {
            vale.setCobranca(null);
        }

        Vale salvo = valeRepository.save(vale);
        return valeMapper.toDTO(salvo);
    }

    public void excluir(Long id) {
        if (!valeRepository.existsById(id)) {
            throw new EntityNotFoundException("Vale não encontrado com id " + id);
        }
        valeRepository.deleteById(id);
    }

    public ValeDTO editar(ValeDTO valeDTO) {
        Vale vale = valeRepository.findById(valeDTO.getId()).orElseThrow(() -> new ResourceNotFoundException("Vale", "id", valeDTO.getId()));
        valeMapper.updateEntityFromDto(valeDTO, vale);
        vale.setFuncionario(funcionarioService.getById(valeDTO.getFuncionarioId()));

        if (valeDTO.getCobrancaId() != null) {
            vale.setCobranca(cobrancasService.getById(valeDTO.getCobrancaId()));
        } else {
            vale.setCobranca(null);
        }
        Vale salvo = valeRepository.save(vale);

        return valeMapper.toDTO(salvo);
    }

    public List<ValeDTO> buscaTodosPorFiltro(ValeQueryFilters filters) {
        List<Vale> valesFiltrados = valeRepository.findAll(filters.toSpecification());

        if (valesFiltrados.isEmpty()) {
            throw new ResourceNotFoundException("Não foi possível encontrar nenhum pix com os filtros especificados!");
        }

        return valesFiltrados.stream().map(valeMapper :: toDTO).toList();

    }

}