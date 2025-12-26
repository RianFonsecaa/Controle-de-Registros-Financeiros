package com.system.controleDeRegistrosFinanceiros.cobranca.service;

import java.util.ArrayList;
import java.util.List;

import com.system.controleDeRegistrosFinanceiros.cidade.service.CidadeService;
import com.system.controleDeRegistrosFinanceiros.exceptions.ResourceNotFoundException;
import com.system.controleDeRegistrosFinanceiros.funcionario.service.FuncionarioService;
import com.system.controleDeRegistrosFinanceiros.veiculo.service.VeiculoService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.system.controleDeRegistrosFinanceiros.authentication.model.User;
import com.system.controleDeRegistrosFinanceiros.cobranca.mapper.CobrancasMapper;
import com.system.controleDeRegistrosFinanceiros.cobranca.model.dto.CobrancaDTO;
import com.system.controleDeRegistrosFinanceiros.cobranca.model.entity.Cobranca;
import com.system.controleDeRegistrosFinanceiros.cobranca.repository.CobrancaRepository;
import com.system.controleDeRegistrosFinanceiros.pix.mapper.PixMapper;
import com.system.controleDeRegistrosFinanceiros.vale.mapper.ValeMapper;
import com.system.controleDeRegistrosFinanceiros.vale.model.dto.ValeDTO;
import com.system.controleDeRegistrosFinanceiros.vale.model.entity.Vale;

import jakarta.persistence.EntityNotFoundException;


@Service
public class CobrancasService {

    private final CobrancaRepository cobrancaRepository;
    private final CobrancasMapper cobrancasMapper;

    private final CidadeService cidadeService;
    private final FuncionarioService funcionarioService;
    private final VeiculoService veiculoService;

    private final ValeMapper valeMapper;

    public CobrancasService(
            CobrancaRepository cobrancaRepository,
            CobrancasMapper cobrancasMapper,
            CidadeService cidadeService,
            FuncionarioService funcionarioService,
            VeiculoService veiculoService,
            PixMapper pixMapper,
            ValeMapper valeMapper) {

        this.cobrancaRepository = cobrancaRepository;
        this.cobrancasMapper = cobrancasMapper;

        this.cidadeService = cidadeService;
        this.funcionarioService = funcionarioService;
        this.veiculoService = veiculoService;

        this.valeMapper = valeMapper;
    }

    public List<CobrancaDTO> buscaTodos() {
        return cobrancaRepository.findAll()
                .stream()
                .map(cobrancasMapper::toDTO)
                .toList();
    }

    public Cobranca getById(Long id) {
        return cobrancaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cobrança não encontrada"));
    }

    public CobrancaDTO salvar(CobrancaDTO cobrancaDTO) {

        User user = (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        Cobranca cobranca = cobrancasMapper.toEntity(cobrancaDTO);

        cobranca.setCidade(
                cidadeService.getById(cobrancaDTO.getCidadeId())
        );

        cobranca.setCobrador(
                funcionarioService.getById(cobrancaDTO.getCobradorId())
        );

        cobranca.setVeiculo(
                veiculoService.getById(cobrancaDTO.getVeiculoId())
        );

        cobranca.setRegistroPor(user.getName());

        cobranca.setVales(associarVales(cobrancaDTO, cobranca));

        cobranca.setValorTotalPix(cobrancaDTO.getValorTotalPix());
        cobranca.setValorTotalVale(cobrancaDTO.getValorTotalVale());

        cobranca.setValorTotal(
                cobranca.getValorTotalEspecie()
                        + cobranca.getValorTotalPix()
                        + cobranca.getValorTotalVale()
        );

        Cobranca salvo = cobrancaRepository.save(cobranca);

        return cobrancasMapper.toDTO(salvo);
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
            throw new ResourceNotFoundException("Cobrança","Id",id);
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
    