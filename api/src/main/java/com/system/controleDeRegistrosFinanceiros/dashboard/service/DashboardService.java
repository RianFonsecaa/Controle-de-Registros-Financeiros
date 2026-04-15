package com.system.controleDeRegistrosFinanceiros.dashboard.service;

import com.system.controleDeRegistrosFinanceiros.cobranca.model.entity.Cobranca;
import com.system.controleDeRegistrosFinanceiros.cobranca.repository.CobrancaRepository;
import com.system.controleDeRegistrosFinanceiros.dashboard.model.*;
import com.system.controleDeRegistrosFinanceiros.pix.model.entity.Pix;
import com.system.controleDeRegistrosFinanceiros.pix.repository.PixRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    private final CobrancaRepository cobrancaRepository;
    private final PixRepository pixRepository;

    public DashboardService(CobrancaRepository cobrancaRepository, PixRepository pixRepository) {
        this.cobrancaRepository = cobrancaRepository;
        this.pixRepository = pixRepository;
    }

public DashboardDTO getDashboardData() {
    // 1. Use LocalDateTime para capturar a data E a hora atual
    LocalDateTime agora = LocalDateTime.now();
    
    // 2. Extraia o LocalDate para usar nas buscas do repositório
    LocalDate hoje = agora.toLocalDate();
    LocalDate inicioMes = hoje.withDayOfMonth(1);
    LocalDate inicioSemana = hoje.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

    // 3. O mês continua funcionando igual
    String mesReferencia = hoje.getMonth().getDisplayName(TextStyle.FULL, Locale.of("pt", "BR"));
    
    // 4. AGORA SIM: O 'agora' (LocalDateTime) possui HourOfDay, MinuteOfHour, etc.
    String ultimaAtualizacao = agora.format(DateTimeFormatter.ofPattern("HH:mm"));

    List<Cobranca> cobrancasDoMes = cobrancaRepository.findByDataBetween(inicioMes, hoje);
    List<Pix> pixsDoMes = pixRepository.findByDataBetween(inicioMes, hoje);

    ResumoGeralDTO resumo = calcularResumoGeral(cobrancasDoMes, pixsDoMes);
    List<RendimentoCidadesDTO> ranking = calcularRankingCidades(cobrancasDoMes);
    List<RendimentoDiarioDTO> diario = calcularRendimentoDiario(cobrancasDoMes, pixsDoMes, inicioSemana);
    MetodoPagamentoDTO pagamentos = calcularDistribuicaoPagamentos(cobrancasDoMes, pixsDoMes);

    return new DashboardDTO(resumo, ranking, diario, pagamentos, mesReferencia.toUpperCase(), ultimaAtualizacao);
}

    private ResumoGeralDTO calcularResumoGeral(List<Cobranca> cobrancas, List<Pix> pixs) {
        double totalPixCobrancas = cobrancas.stream().mapToDouble(c -> c.getValorTotalPix() != null ? c.getValorTotalPix() : 0.0).sum();
        double totalPixAvulsos = pixs.stream().mapToDouble(p -> p.getValor() != null ? p.getValor() : 0.0).sum();
        
        double totalPixTotal = totalPixCobrancas + totalPixAvulsos;
        double totalEspecie = cobrancas.stream().mapToDouble(Cobranca::getValorEspecie).sum();
        
        double totalGeral = totalEspecie + totalPixTotal; 
        
        long cidadesAtendidas = cobrancas.stream().map(c -> c.getCidade().getId()).distinct().count();

        return new ResumoGeralDTO(arredondar(totalGeral), arredondar(totalPixTotal), arredondar(totalEspecie), cidadesAtendidas);
    }

    private List<RendimentoDiarioDTO> calcularRendimentoDiario(List<Cobranca> cobrancas, List<Pix> pixs, LocalDate inicioSemana) {
        Map<DayOfWeek, Double> mapaDias = cobrancas.stream()
                .filter(c -> !c.getData().isBefore(inicioSemana))
                .collect(Collectors.groupingBy(
                        c -> c.getData().getDayOfWeek(),
                        Collectors.summingDouble(Cobranca::getValorTotal)
                ));

        pixs.stream()
                .filter(p -> !p.getData().isBefore(inicioSemana))
                .forEach(p -> {
                    DayOfWeek dia = p.getData().getDayOfWeek();
                    mapaDias.put(dia, mapaDias.getOrDefault(dia, 0.0) + (p.getValor() != null ? p.getValor() : 0.0));
                });

        return Arrays.stream(DayOfWeek.values())
                .map(dia -> new RendimentoDiarioDTO(
                        traduzirDia(dia),
                        arredondar(mapaDias.getOrDefault(dia, 0.0))
                ))
                .toList();
    }

    public MetodoPagamentoDTO calcularDistribuicaoPagamentos(List<Cobranca> cobrancas, List<Pix> pixs) {
        double totalPixCobrancas = cobrancas.stream()
                .mapToDouble(c -> c.getValorTotalPix() != null ? c.getValorTotalPix() : 0.0)
                .sum();

        double totalPixAvulsos = pixs.stream()
                .mapToDouble(p -> p.getValor() != null ? p.getValor() : 0.0)
                .sum();

        double totalEspecie = cobrancas.stream()
                .mapToDouble(c -> c.getValorEspecie() != null ? c.getValorEspecie() : 0.0)
                .sum();

        return new MetodoPagamentoDTO(arredondar(totalPixCobrancas + totalPixAvulsos), arredondar(totalEspecie));
    }

    private List<RendimentoCidadesDTO> calcularRankingCidades(List<Cobranca> cobrancas) {
        return cobrancas.stream()
                .collect(Collectors.groupingBy(c -> c.getCidade().getNome()))
                .entrySet().stream()
                .map(entry -> {
                    String nomeCidade = entry.getKey();
                    double total = entry.getValue().stream().mapToDouble(Cobranca::getValorTotal).sum();
                    return new RendimentoCidadesDTO(nomeCidade, arredondar(total));
                })
                .sorted(Comparator.comparing(RendimentoCidadesDTO::total).reversed())
                .toList();
    }

    private String traduzirDia(DayOfWeek dia) {
        return dia.getDisplayName(TextStyle.FULL, Locale.of("pt", "BR"));
    }

    private double arredondar(double valor) {
        return Math.round(valor * 100.0) / 100.0;
    }
}