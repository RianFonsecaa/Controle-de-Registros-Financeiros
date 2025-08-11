package com.system.controleDeRegistrosFinanceiros.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.system.controleDeRegistrosFinanceiros.mapper.CobrancasMapper;
import com.system.controleDeRegistrosFinanceiros.mapper.DespesaMapper;
import com.system.controleDeRegistrosFinanceiros.mapper.PixMapper;
import com.system.controleDeRegistrosFinanceiros.mapper.ValeMapper;
import com.system.controleDeRegistrosFinanceiros.model.dtos.CobrancaDTO;
import com.system.controleDeRegistrosFinanceiros.model.dtos.DespesaDTO;
import com.system.controleDeRegistrosFinanceiros.model.dtos.PixDTO;
import com.system.controleDeRegistrosFinanceiros.model.dtos.ValeDTO;
import com.system.controleDeRegistrosFinanceiros.model.entidades.Cidade;
import com.system.controleDeRegistrosFinanceiros.model.entidades.Cobranca;
import com.system.controleDeRegistrosFinanceiros.model.entidades.Despesa;
import com.system.controleDeRegistrosFinanceiros.model.entidades.Funcionario;
import com.system.controleDeRegistrosFinanceiros.model.entidades.Pix;
import com.system.controleDeRegistrosFinanceiros.model.entidades.Vale;
import com.system.controleDeRegistrosFinanceiros.repository.CidadeRepository;
import com.system.controleDeRegistrosFinanceiros.repository.CobrancaRepository;
import com.system.controleDeRegistrosFinanceiros.repository.FuncionarioRepository;
import com.system.controleDeRegistrosFinanceiros.services.interfaces.CobrancasService;

@Service
public class CobrancasServiceImpl implements CobrancasService {

    private final CobrancaRepository cobrancaRepository;
    private final CobrancasMapper cobrancasMapper;
    private final CidadeRepository cidadeRepository;
    private final FuncionarioRepository funcionarioRepository;

    private final PixMapper pixMapper;
    private final ValeMapper valeMapper;
    private final DespesaMapper despesaMapper;

    public CobrancasServiceImpl(
        CobrancaRepository cobrancaRepository, 
        CobrancasMapper cobrancasMapper, 
        CidadeRepository cidadeRepository,
        FuncionarioRepository funcionarioRepository, 
        PixMapper pixMapper, 
        ValeMapper valeMapper, 
        DespesaMapper despesaMapper) {

        this.cobrancaRepository = cobrancaRepository;
        this.cobrancasMapper = cobrancasMapper;
        this.cidadeRepository = cidadeRepository;
        this.funcionarioRepository = funcionarioRepository;
        this.pixMapper = pixMapper;
        this.valeMapper = valeMapper;
        this.despesaMapper = despesaMapper;
    }

    @Override
    public List<CobrancaDTO> getTodos() {
        return cobrancaRepository.findAll()
                .stream()
                .map(cobrancasMapper::toDTO)
                .toList();
    }

    @Override
    public CobrancaDTO buscarPorId(Long id) {
        Cobranca cobranca = cobrancaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Cobrança não encontrada"));
        return cobrancasMapper.toDTO(cobranca);
    }

    @Override
    public CobrancaDTO salvar(CobrancaDTO cobrancaDTO) {
        Cidade cidade = cidadeRepository.findById(cobrancaDTO.getCidade().getId())
            .orElseThrow(() -> new RuntimeException("Cidade não encontrada"));

        Funcionario cobrador = funcionarioRepository.findById(cobrancaDTO.getCobrador().getId())
            .orElseThrow(() -> new RuntimeException("funcionario não encontrado"));

        Cobranca cobranca = cobrancasMapper.toEntity(cobrancaDTO);

        cobranca.setCidade(cidade);
        cobranca.setCobrador(cobrador);
        associarPix(cobrancaDTO, cobranca);
        associarVales(cobrancaDTO, cobranca);
        associarDespesas(cobrancaDTO, cobranca);

        Cobranca salvo = cobrancaRepository.save(cobranca);
        return cobrancasMapper.toDTO(salvo);
    }

    private void associarPix(CobrancaDTO dto, Cobranca cobranca) {
        cobranca.setPix(new ArrayList<>());
        if (dto.getPix() != null && !dto.getPix().isEmpty()) {
            for (PixDTO pixDTO : dto.getPix()) {
                Pix pix = pixMapper.toEntity(pixDTO);
                pix.setCobranca(cobranca);
                pix.setCidade(cobranca.getCidade());
                cobranca.getPix().add(pix);
            }
        }
    }

    private void associarVales(CobrancaDTO dto, Cobranca cobranca) {
        cobranca.setVales(new ArrayList<>());
        if (dto.getVales() != null && !dto.getVales().isEmpty()) {
            for (ValeDTO valeDTO : dto.getVales()) {
                Vale vale = valeMapper.toEntity(valeDTO);
                vale.setCobranca(cobranca);
                cobranca.getVales().add(vale);
            }
        }
    }

    private void associarDespesas(CobrancaDTO dto, Cobranca cobranca) {
        cobranca.setDespesas(new ArrayList<>());
        if (dto.getDespesas() != null && !dto.getDespesas().isEmpty()) {
            for (DespesaDTO despesaDTO : dto.getDespesas()) {
                Despesa despesa = despesaMapper.toEntity(despesaDTO);
                despesa.setCobranca(cobranca);
                cobranca.getDespesas().add(despesa);
            }
        }
    }

    @Override
    public CobrancaDTO atualizar(Long id, CobrancaDTO cobrancaDTO) {
        Cobranca existente = cobrancaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Cobrança não encontrada"));
        
        Cobranca cobrancaAtualizada = cobrancasMapper.toEntity(cobrancaDTO);
        cobrancaAtualizada.setId(existente.getId());
        Cobranca salvo = cobrancaRepository.save(cobrancaAtualizada);
        return cobrancasMapper.toDTO(salvo);
    }

    @Override
    public void deletar(Long id) {
        if (!cobrancaRepository.existsById(id)) {
            throw new RuntimeException("Cobrança não encontrada");
        }
        cobrancaRepository.deleteById(id);
    }
}