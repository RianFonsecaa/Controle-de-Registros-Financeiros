package com.system.controleDeRegistrosFinanceiros.whatsapp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.system.controleDeRegistrosFinanceiros.whatsapp.service.WhatsappBotService;

import java.util.Map;

@RestController
@RequestMapping("/api/whatsapp")
public class WhatsappWebhookController {

    private final WhatsappBotService botService;

    public WhatsappWebhookController(WhatsappBotService botService) {
        this.botService = botService;
    }

    @PostMapping("/webhook")
    public ResponseEntity<Void> receber(@RequestBody Map<String, Object> request) {
        String evento = (String) request.get("event");
        Map<String, Object> payload = (Map<String, Object>) request.get("payload");

        if ("message".equals(evento) && payload != null) {
            Boolean deMim = (Boolean) payload.get("fromMe");
            if (Boolean.TRUE.equals(deMim)) {
                return ResponseEntity.ok().build();
            }
            botService.processarMensagem(payload);
        }
        return ResponseEntity.ok().build();
    }
}