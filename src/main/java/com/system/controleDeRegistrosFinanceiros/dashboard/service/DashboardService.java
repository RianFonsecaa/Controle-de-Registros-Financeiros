package com.system.controleDeRegistrosFinanceiros.dashboard.service;

import com.system.controleDeRegistrosFinanceiros.cobranca.model.entity.Cobranca;
import com.system.controleDeRegistrosFinanceiros.cobranca.repository.CobrancaRepository;
import com.system.controleDeRegistrosFinanceiros.dashboard.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    private final CobrancaRepository repository;

    public DashboardService(CobrancaRepository repository) {
        this.repository = repository;
    }

    public DashboardDTO getDashboardData() {
        LocalDate hoje = LocalDate.now();
        LocalDate inicioMes = hoje.withDayOfMonth(1);
        LocalDate inicioSemana = hoje.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

        List<Cobranca> cobrancasDoMes = repository.findByDataBetween(inicioMes, hoje);

        ResumoGeralDTO resumo = calcularResumoGeral(cobrancasDoMes);

        List<RendimentoCidadesDTO> ranking = calcularRankingCidades(cobrancasDoMes);

        List<RendimentoDiarioDTO> diario = calcularRendimentoDiario(cobrancasDoMes, inicioSemana);

        MetodoPagamentoDTO pagamentos = calcularDistribuicaoPagamentos(cobrancasDoMes);

        DashboardDTO dashboardDTO = new DashboardDTO(resumo, ranking, diario, pagamentos);
        return dashboardDTO;
    }

    private ResumoGeralDTO calcularResumoGeral(List<Cobranca> cobrancas) {
        double totalPix = cobrancas.stream().mapToDouble(c -> c.getValorTotalPix() != null ? c.getValorTotalPix() : 0.0).sum();
        double totalEspecie = cobrancas.stream().mapToDouble(Cobranca::getValorEspecie).sum();
        double totalGeral = cobrancas.stream().mapToDouble(Cobranca::getValorTotal).sum();
        long cidadesAtendidas = cobrancas.stream().map(c -> c.getCidade().getId()).distinct().count();

        return new ResumoGeralDTO(arredondar(totalGeral), arredondar(totalPix), arredondar(totalEspecie), cidadesAtendidas);
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

    private List<RendimentoDiarioDTO> calcularRendimentoDiario(List<Cobranca> cobrancas, LocalDate inicioSemana) {
        Map<DayOfWeek, Double> mapaDias = cobrancas.stream()
                .filter(c -> !c.getData().isBefore(inicioSemana))
                .collect(Collectors.groupingBy(
                        c -> c.getData().getDayOfWeek(),
                        Collectors.summingDouble(Cobranca::getValorTotal)
                ));
        return Arrays.stream(DayOfWeek.values())
                .map(dia -> new RendimentoDiarioDTO(
                        traduzirDia(dia),
                        arredondar(mapaDias.getOrDefault(dia, 0.0))
                ))
                .toList();
    }

    public MetodoPagamentoDTO calcularDistribuicaoPagamentos(List<Cobranca> cobrancas) {
        double totalPix = cobrancas.stream()
                .mapToDouble(c -> c.getValorTotalPix() != null ? c.getValorTotalPix() : 0.0)
                .sum();

        double totalEspecie = cobrancas.stream()
                .mapToDouble(c -> c.getValorEspecie() != null ? c.getValorEspecie() : 0.0)
                .sum();

        return new MetodoPagamentoDTO(arredondar(totalPix), arredondar(totalEspecie));
    }

    private String traduzirDia(DayOfWeek dia) {
        return dia.getDisplayName(TextStyle.FULL, Locale.of("pt", "BR"));
    }

    private double arredondar(double valor) {
        return Math.round(valor * 100.0) / 100.0;
    }
}