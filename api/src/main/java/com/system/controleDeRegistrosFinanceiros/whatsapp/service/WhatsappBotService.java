package com.system.controleDeRegistrosFinanceiros.whatsapp.service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.system.controleDeRegistrosFinanceiros.cobranca.model.dto.CobrancaQueryFilters;
import com.system.controleDeRegistrosFinanceiros.relatorio.service.RelatorioService;

@Service
public class WhatsappBotService {

    @Value("${WHATSAPP_API_KEY}")
    private String apiKey;

    private final String WAHA_URL = "http://waha:3000/api/sendText";

    private final int arquivoExpiraEm = 10;

    private final Set<String> mensagensProcessadas = 
            Collections.newSetFromMap(new ConcurrentHashMap<String, Boolean>());

    private RelatorioService relatorioService;

    public WhatsappBotService(RelatorioService relatorioService){
        this.relatorioService = relatorioService;
    }

   public void processarMensagem(Map<String, Object> payload) {
        String de = (String) payload.get("from");
        String textoRaw = (String) payload.get("body");
        String messageId = (String) payload.get("id");

        if (textoRaw == null || mensagensProcessadas.contains(messageId)) return;
        mensagensProcessadas.add(messageId);
        
        String comando = textoRaw.trim().toLowerCase();

        switch (comando) {
            case "1": 
                processarFiltro(de, LocalDate.now(), LocalDate.now(), "hoje", "Hoje"); 
                break;
            case "2": 
                processarFiltro(de, LocalDate.now().minusDays(1), LocalDate.now().minusDays(1), "ontem", "Ontem"); 
                break;
            case "3": 
                processarFiltro(de, LocalDate.now().minusDays(7), LocalDate.now(), "ultimos_7_dias", "Últimos 7 Dias"); 
                break;
            case "4": 
                processarFiltro(de, LocalDate.now().withDayOfMonth(1), LocalDate.now(), "mes_atual", "Mês Atual"); 
                break;
            case "menu":
            case "oi":
                enviarResposta(de, "👋 *Olá! Sou o Assistente da Comercial Fonseca.*\n\n"+
                "Escolha uma opção para gerar o relatório de cobranças:\n\n"+
                "*1️⃣ Hoje*\n"+
                "*2️⃣ Ontem*\n"+
                "*3️⃣ 7 Dias*\n"+
                "*4️⃣ Mês Atual*\n"+
                "-----\n"+
                "💡 _Os links expiram em " + arquivoExpiraEm +" minutos por segurança!_");
                break;
            default:
                enviarResposta(de, "❌ Opção inválida. Digite *menu*.");
                break;
        }
    }

    private void processarFiltro(String de, LocalDate inicio, LocalDate fim, String tituloArquivo, String tituloMensagem) {
        DateTimeFormatter brFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter fileFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        enviarResposta(de, "⏳ Gerando relatório de *" + tituloMensagem + "*... Aguarde.");

        try {
            CobrancaQueryFilters filtros = new CobrancaQueryFilters();
            filtros.setDataInicio(inicio);
            filtros.setDataFim(fim);

            byte[] pdf = relatorioService.gerarRelatorioParaWhatsapp(filtros, "Bot Comercial Fonseca");
            
            String dataInicio = inicio.format(fileFormatter);
            String nomeArquivo = "rel_" + tituloArquivo+ "_" + dataInicio + "_" + System.currentTimeMillis() + ".pdf";
            
            String link = relatorioService.fazerUploadParaSupabase(pdf, nomeArquivo);

            if (link != null) {
                String msgLink = String.format("📑 *Relatório: %s*\n📅 Data: %s\n\n✅ Link:\n%s", 
                                                tituloMensagem, inicio.format(brFormatter), link);
                enviarResposta(de, msgLink);
                relatorioService.agendarExclusaoDocumentoSupabase(nomeArquivo, arquivoExpiraEm);
            }
        } catch (Exception e) {
            System.err.println("Erro ao gerar relatório: " + e.getMessage());
            enviarResposta(de, "❌ Erro ao gerar o relatório ou dados não encontrados.");
        }
    }

    private void enviarResposta(String para, String mensagem) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            Map<String, String> body = new HashMap<>();
            body.put("session", "default");
            body.put("chatId", para);
            body.put("text", mensagem);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("X-Api-Key", apiKey);

            HttpEntity<Map<String, String>> entity = new HttpEntity<>(body, headers);
            restTemplate.postForEntity(WAHA_URL, entity, String.class);
            System.out.println(">>> RESPOSTA ENVIADA PARA: " + para);
        } catch (Exception e) {
            System.err.println(">>> ERRO WAHA: " + e.getMessage());
        }
    }

}
