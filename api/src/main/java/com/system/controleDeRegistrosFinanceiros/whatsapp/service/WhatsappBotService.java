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
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.system.controleDeRegistrosFinanceiros.cobranca.model.dto.CobrancaQueryFilters;
import com.system.controleDeRegistrosFinanceiros.relatorio.service.RelatorioService;
import com.system.controleDeRegistrosFinanceiros.usuario.service.UsuarioService;
import com.system.controleDeRegistrosFinanceiros.whatsapp.model.EnviarRelatorioRequest;

@Service
public class WhatsappBotService {

    @Value("${WHATSAPP_API_KEY}")
    private String apiKey;

    private final String WAHA_URL = "http://waha:3000/api/sendText";

    private final int arquivoExpiraEm = 15;

    private final Set<String> mensagensProcessadas = 
            Collections.newSetFromMap(new ConcurrentHashMap<String, Boolean>());

    private RelatorioService relatorioService;

    private UsuarioService usuarioService;

    private final RestTemplate restTemplate = new RestTemplate();

    public WhatsappBotService(RelatorioService relatorioService, UsuarioService usuarioService){
        this.relatorioService = relatorioService;
        this.usuarioService = usuarioService;
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
                geraRelatorioPorData(de, LocalDate.now(), LocalDate.now(), "hoje", "Hoje"); 
                break;
            case "2": 
                geraRelatorioPorData(de, LocalDate.now().minusDays(1), LocalDate.now().minusDays(1), "ontem", "Ontem"); 
                break;
            case "3": 
                geraRelatorioPorData(de, LocalDate.now().minusDays(7), LocalDate.now(), "ultimos_7_dias", "Últimos 7 Dias"); 
                break;
            case "4": 
                geraRelatorioPorData(de, LocalDate.now().withDayOfMonth(1), LocalDate.now(), "mes_atual", "Mês Atual"); 
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

    private void geraRelatorioPorData(String de, LocalDate inicio, LocalDate fim, String tituloArquivo, String tituloMensagem) {
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

    @Async
    public void enviarRelatorioParaLista(EnviarRelatorioRequest request) {
        try {
            byte[] pdf = relatorioService.gerarRelatorioParaWhatsapp(request.filtros(), "Comercial Fonseca");
            
            String nomeArquivo = "rel_" + System.currentTimeMillis() + ".pdf";
            String link = relatorioService.fazerUploadParaSupabase(pdf, nomeArquivo);

            for (String numero : request.numerosTelefone()) {
                String formatado = numero.replaceAll("[^0-9]", "") + "@c.us";
                String mensagem = request.mensagemPersonalizada() + "\n\nLink do Relatório:\n" + link;
                
                enviarResposta(formatado, mensagem);
                Thread.sleep(1000); 
            }
            
            relatorioService.agendarExclusaoDocumentoSupabase(nomeArquivo, arquivoExpiraEm);

       } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Thread de envio interrompida: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Erro no envio em lote: " + e.getMessage());
        }
    }

    private void enviarResposta(String para, String mensagem) {
        try {
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

    public String buscarNumeroPorLid(String lid) {
        try {
            String lidLimpo = lid.split("@")[0];
            String url = "http://waha:3000/api/default/lids/" + lidLimpo;

            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-Api-Key", apiKey);
            HttpEntity<Void> entity = new HttpEntity<>(headers);

            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return (String) response.getBody().get("pn");
            }
        } catch (Exception e) {
            System.err.println("Erro ao traduzir LID: " + e.getMessage());
        }
        return null;
    }

    public boolean validarEProcessar(Map<String, Object> payload) {
        String from = (String) payload.get("from");
        if (from == null) return false;

        String numeroParaValidar = identificarRemetente(from);
        if (numeroParaValidar == null) return false;

        String numeroLimpo = limparNumero(numeroParaValidar);
        boolean autorizado = usuarioService.buscaTodos().stream()
                .anyMatch(u -> u.telefone() != null && 
                               compararNumeros(numeroLimpo, limparNumero(u.telefone()))) ;

        if (autorizado) {
            processarMensagem(payload);
            return true;
        }

        System.out.println(">>> Bloqueado: Acesso negado para o número: " + numeroLimpo);
        return false;
    }

    private String identificarRemetente(String from) {
        if (from.contains("@lid")) {
            System.out.println(">>> Traduzindo LID: " + from);
            return buscarNumeroPorLid(from);
        }
        return from;
    }

    private String limparNumero(String numero) {
        return numero != null ? numero.replaceAll("[^0-9]", "") : "";
    }

    private boolean compararNumeros(String numeroWpp, String numeroBanco) {
        if (numeroWpp.length() < 8 || numeroBanco.length() < 8) return false;
        String ultimos8Wpp = numeroWpp.substring(numeroWpp.length() - 8);
        String ultimos8Banco = numeroBanco.substring(numeroBanco.length() - 8);
        return ultimos8Wpp.equals(ultimos8Banco);
    }

}
