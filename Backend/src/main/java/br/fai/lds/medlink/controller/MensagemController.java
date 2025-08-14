package br.fai.lds.medlink.controller;

import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/mensagens")
@CrossOrigin
public class MensagemController {

    private static List<Map<String, Object>> mensagens = new ArrayList<>();
    
    static {
        mensagens.add(Map.of(
            "id", "1",
            "remetenteId", "1",
            "remetenteTipo", "paciente",
            "remetenteNome", "João da Silva",
            "destinatarioId", "medico_1",
            "destinatarioTipo", "medico",
            "destinatarioNome", "Dr. Carlos Silva",
            "texto", "Olá, doutor! Gostaria de agendar uma consulta.",
            "data", "2024-08-04T10:00:00",
            "lida", false
        ));
    }

    @GetMapping
    public List<Map<String, Object>> getMensagens() {
        return mensagens;
    }

    @PostMapping
    public Map<String, Object> enviarMensagem(@RequestBody Map<String, Object> mensagem) {
        mensagem.put("id", String.valueOf(System.currentTimeMillis()));
        mensagem.put("data", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        mensagem.put("lida", false);
        mensagens.add(mensagem);
        return mensagem;
    }
}