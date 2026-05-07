package com.system.controleDeRegistrosFinanceiros.whatsapp;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/whatsapp")
public class WhatsappWebhookController {

    private static final Logger logger = LoggerFactory.getLogger(WhatsappWebhookController.class);

    @PostMapping("/webhook")
    public ResponseEntity<Void> receberMensagem(@RequestBody(required = false) Map<String, Object> request) {
        try {
            // 1. Verifica se o corpo da requisição não está vazio
            if (request == null || request.isEmpty()) {
                logger.warn("Recebido webhook vazio ou sem corpo.");
                return ResponseEntity.ok().build();
            }

            // 2. Extrai o evento e o payload conforme a documentação do WAHA
            String evento = (String) request.get("event");
            Map<String, Object> payload = (Map<String, Object>) request.get("payload");

            logger.info("Evento recebido: {}", evento);

            // 3. Processa apenas se for um evento de mensagem
            if ("message".equals(evento) && payload != null) {
                String de = (String) payload.get("from");    // Número do remetente
                String texto = (String) payload.get("body"); // Conteúdo da mensagem
                Long dataEnvio = (Long) payload.get("timestamp"); 
                Boolean deMim = (Boolean) payload.get("fromMe"); // Se a mensagem foi enviada por você

                // Evita processar mensagens que você mesmo enviou (loop infinito)
                if (Boolean.TRUE.equals(deMim)) {
                    return ResponseEntity.ok().build();
                }

                System.out.println("\n========================================");
                System.out.println("MENSAGEM RECEBIDA VIA WHATSAPP");
                System.out.println("Remetente: " + de);
                System.out.println("Texto: " + texto);
                System.out.println("Hora: " + dataEnvio);
                System.out.println("========================================\n");
            }

            // 4. Retorna sempre 200 OK para o WAHA não tentar reenviar a mesma mensagem
            return ResponseEntity.ok().build();

        } catch (Exception e) {
            logger.error("Erro ao processar webhook do WhatsApp: {}", e.getMessage());
            // Mesmo em erro, retornamos 200 para evitar retentivas infinitas do gateway durante testes
            return ResponseEntity.ok().build();
        }
    }
}
