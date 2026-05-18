package com.system.controleDeRegistrosFinanceiros.whatsapp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.system.controleDeRegistrosFinanceiros.usuario.service.UsuarioService;
import com.system.controleDeRegistrosFinanceiros.whatsapp.model.RelatorioWppRequestDTO;
import com.system.controleDeRegistrosFinanceiros.whatsapp.service.WhatsappBotService;

import java.util.Map;

@RestController
@RequestMapping("/api/whatsapp")
public class WhatsappWebhookController {

    private final WhatsappBotService whatsappBotService;

    

    public WhatsappWebhookController(WhatsappBotService whatsappBotService, UsuarioService usuarioService) {
        this.whatsappBotService = whatsappBotService;
        
    }

    @PostMapping("/webhook")
    public ResponseEntity<Void> receber(@RequestBody Map<String, Object> request) {
        String evento = (String) request.get("event");
        Map<String, Object> payload = (Map<String, Object>) request.get("payload");

        if ("message".equals(evento) && payload != null) {
            if (Boolean.TRUE.equals(payload.get("fromMe"))) {
                return ResponseEntity.ok().build();
            }
            whatsappBotService.validarEProcessar(payload);
        }

        return ResponseEntity.ok().build();
    }

    @PostMapping("/envia-relatorio-lista")
    public ResponseEntity<String> dispararRelatorios(@RequestBody RelatorioWppRequestDTO request) {
        whatsappBotService.enviarRelatorioParaLista(request);
        return ResponseEntity.ok("Processo de envio iniciado com sucesso!");
    }
}