package com.system.controleDeRegistrosFinanceiros.cobranca.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.system.controleDeRegistrosFinanceiros.cidade.model.entity.Cidade;
import com.system.controleDeRegistrosFinanceiros.cidade.repository.CidadeRepository;
import com.system.controleDeRegistrosFinanceiros.cobranca.mapper.CobrancasMapper;
import com.system.controleDeRegistrosFinanceiros.cobranca.model.dto.CobrancaDTO;
import com.system.controleDeRegistrosFinanceiros.cobranca.model.entity.Cobranca;
import com.system.controleDeRegistrosFinanceiros.cobranca.repository.CobrancaRepository;
import com.system.controleDeRegistrosFinanceiros.cobranca.service.interfaces.CobrancasService;
import com.system.controleDeRegistrosFinanceiros.funcionario.model.Funcionario;
import com.system.controleDeRegistrosFinanceiros.funcionario.repository.FuncionarioRepository;
import com.system.controleDeRegistrosFinanceiros.pix.mapper.PixMapper;
import com.system.controleDeRegistrosFinanceiros.pix.model.Pix;
import com.system.controleDeRegistrosFinanceiros.pix.model.PixDTO;
import com.system.controleDeRegistrosFinanceiros.relatorio.model.CobrancaDiaria;
import com.system.controleDeRegistrosFinanceiros.relatorio.service.RelatorioService;
import com.system.controleDeRegistrosFinanceiros.vale.mapper.ValeMapper;
import com.system.controleDeRegistrosFinanceiros.vale.model.Vale;
import com.system.controleDeRegistrosFinanceiros.vale.model.ValeDTO;


@Service
public class CobrancasServiceImpl implements CobrancasService {

    private final CobrancaRepository cobrancaRepository;
    private final CobrancasMapper cobrancasMapper;
    private final CidadeRepository cidadeRepository;
    private final FuncionarioRepository funcionarioRepository;
    private final RelatorioService relatorioService;

    private final PixMapper pixMapper;
    private final ValeMapper valeMapper;

    public CobrancasServiceImpl(
        CobrancaRepository cobrancaRepository, 
        CobrancasMapper cobrancasMapper, 
        CidadeRepository cidadeRepository,
        FuncionarioRepository funcionarioRepository, 
        PixMapper pixMapper, 
        ValeMapper valeMapper,
        RelatorioService relatorioService) {

        this.cobrancaRepository = cobrancaRepository;
        this.cobrancasMapper = cobrancasMapper;
        this.cidadeRepository = cidadeRepository;
        this.funcionarioRepository = funcionarioRepository;
        this.pixMapper = pixMapper;
        this.valeMapper = valeMapper;
        this.relatorioService = relatorioService;
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

        Cobranca salvo = cobrancaRepository.save(cobranca);

        try {
            List<Cobranca> listaCobrancasDiarias = cobrancaRepository.findAllByData(LocalDate.now());

            List<CobrancaDiaria> dadosParaRelatorio = listaCobrancasDiarias.stream()
                                                                     .map(CobrancaDiaria::new)
                                                                     .collect(Collectors.toList());
            
            if (!dadosParaRelatorio.isEmpty()) {
                relatorioService.gerarRelatorioCobranca(dadosParaRelatorio);
            }

        } catch (Exception e) {
            System.err.println("Falha ao gerar o relatório após salvar a cobrança. Erro: " + e.getMessage());
            e.printStackTrace();
        }
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

    
}