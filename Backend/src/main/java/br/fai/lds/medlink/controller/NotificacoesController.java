package br.fai.lds.medlink.controller;

import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/notificacoes")
@CrossOrigin
public class NotificacoesController {

    @GetMapping
    public List<Map<String, String>> getNotificacoes() {
        return List.of(
            Map.of(
                "id", "1",
                "icone", "https://cdn-icons-png.flaticon.com/512/1827/1827392.png",
                "mensagem", "Nova atualização disponível no sistema."
            ),
            Map.of(
                "id", "2",
                "icone", "https://cdn-icons-png.flaticon.com/512/1827/1827392.png",
                "mensagem", "Consulta agendada para amanhã às 10h."
            )
        );
    }
}