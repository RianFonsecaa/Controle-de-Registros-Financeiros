package com.system.controleDeRegistrosFinanceiros.cobranca.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.system.controleDeRegistrosFinanceiros.cidade.model.entity.Cidade;
import com.system.controleDeRegistrosFinanceiros.cidade.repository.CidadeRepository;
import com.system.controleDeRegistrosFinanceiros.cobranca.mapper.CobrancasMapper;
import com.system.controleDeRegistrosFinanceiros.cobranca.model.dto.CobrancaDTO;
import com.system.controleDeRegistrosFinanceiros.cobranca.model.entity.Cobranca;
import com.system.controleDeRegistrosFinanceiros.cobranca.repository.CobrancaRepository;
import com.system.controleDeRegistrosFinanceiros.funcionario.model.entity.Funcionario;
import com.system.controleDeRegistrosFinanceiros.funcionario.repository.FuncionarioRepository;
import com.system.controleDeRegistrosFinanceiros.pix.mapper.PixMapper;
import com.system.controleDeRegistrosFinanceiros.pix.model.dto.PixDTO;
import com.system.controleDeRegistrosFinanceiros.pix.model.entity.Pix;
import com.system.controleDeRegistrosFinanceiros.vale.mapper.ValeMapper;
import com.system.controleDeRegistrosFinanceiros.vale.model.dto.ValeDTO;
import com.system.controleDeRegistrosFinanceiros.vale.model.entity.Vale;

import jakarta.persistence.EntityNotFoundException;


@Service
public class CobrancasService {

    private final CobrancaRepository cobrancaRepository;
    private final CobrancasMapper cobrancasMapper;
    private final CidadeRepository cidadeRepository;
    private final FuncionarioRepository funcionarioRepository;
    private final PixMapper pixMapper;
    private final ValeMapper valeMapper;

    public CobrancasService(
            CobrancaRepository cobrancaRepository, 
            CobrancasMapper cobrancasMapper, 
            CidadeRepository cidadeRepository,
            FuncionarioRepository funcionarioRepository, 
            PixMapper pixMapper,
            ValeMapper valeMapper) {
        this.cobrancaRepository = cobrancaRepository;
        this.cobrancasMapper = cobrancasMapper;
        this.cidadeRepository = cidadeRepository;
        this.funcionarioRepository = funcionarioRepository;
        this.pixMapper = pixMapper;
        this.valeMapper = valeMapper;
    }

    public List<CobrancaDTO> buscaTodos() {
        return cobrancaRepository.findAll()
                .stream()
                .map(cobrancasMapper::toDTO)
                .toList();
    }

    public CobrancaDTO buscarPorId(Long id) {
        Cobranca cobranca = cobrancaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cobrança não encontrada"));
        return cobrancasMapper.toDTO(cobranca);
    }

    public CobrancaDTO salvar(CobrancaDTO cobrancaDTO) {
        Cidade cidade = cidadeRepository.findById(cobrancaDTO.getCidade().getId())
                .orElseThrow(() -> new RuntimeException("Cidade não encontrada"));

        Funcionario cobrador = funcionarioRepository.findById(cobrancaDTO.getCobrador().getId())
                .orElseThrow(() -> new RuntimeException("Funcionário não encontrado"));

        Cobranca cobranca = cobrancasMapper.toEntity(cobrancaDTO);
        cobranca.setCidade(cidade);
        cobranca.setCobrador(cobrador);
        cobranca.setPix(associarPix(cobrancaDTO, cobranca));
        cobranca.setVales(associarVales(cobrancaDTO, cobranca));
        cobranca.setValorTotalPix(calcularTotalPix(cobrancaDTO));
        cobranca.setValorTotalVale(calcularTotalVale(cobrancaDTO));
        cobranca.setValorTotal(cobranca.getValorEspecie() 
                                + calcularTotalPix(cobrancaDTO) 
                                + calcularTotalVale(cobrancaDTO));

        Cobranca salvo = cobrancaRepository.save(cobranca);
        
        return cobrancasMapper.toDTO(salvo);
    }
    
    private double calcularTotalPix(CobrancaDTO dto) {
        return (dto.getPix() == null) ? 0.0 : dto.getPix().stream()
                .mapToDouble(PixDTO::getValor)
                .sum();
    }

    private double calcularTotalVale(CobrancaDTO dto) {
        return (dto.getVales() == null) ? 0.0 : dto.getVales().stream()
                .mapToDouble(ValeDTO::getValor)
                .sum();
    }

    private List<Pix> associarPix(CobrancaDTO dto, Cobranca cobranca) {
        List<Pix> novaListaDePix = new ArrayList<>();
        if (dto.getPix() != null && !dto.getPix().isEmpty()) {
            for (PixDTO pixDTO : dto.getPix()) {
                Pix pix = pixMapper.toEntity(pixDTO);
                pix.setCobranca(cobranca);
                pix.setCidade(cobranca.getCidade()); 
                novaListaDePix.add(pix);
            }
        }
        return novaListaDePix;
    }

    private List<Vale> associarVales(CobrancaDTO dto, Cobranca cobranca) {
        List<Vale> novaListaDeVales = new ArrayList<>();
        if (dto.getVales() != null && !dto.getVales().isEmpty()) {
            for (ValeDTO valeDTO : dto.getVales()) {
                Vale vale = valeMapper.toEntity(valeDTO);
                vale.setCobranca(cobranca);
                novaListaDeVales.add(vale);
            }
        }
        return novaListaDeVales;
    }


    public void excluir(Long id){
        if (!cobrancaRepository.existsById(id)){
            throw new EntityNotFoundException("Não foi possível excluir. Cobrança com ID " + id + " não encontrada.");
        };

        cobrancaRepository.deleteById(id);
    }

    public CobrancaDTO editar(CobrancaDTO cobrancaDTO){
        if (cobrancaDTO.getId() == null){
            throw new EntityNotFoundException("Não foi possível editar. Cobrança com ID " + cobrancaDTO.getId() + " não encontrada.");
        }
        return this.salvar(cobrancaDTO);
    }
}
    