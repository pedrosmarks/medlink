package br.fai.lds.medlink.controller;

import br.fai.lds.medlink.domain.ApiResponse;
import br.fai.lds.medlink.domain.Mensagem;
import br.fai.lds.medlink.implementation.service.message.MensagemServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/messages")
@CrossOrigin
public class MessageController {

    private final MensagemServiceImpl mensagemService;

    public MessageController(MensagemServiceImpl mensagemService) {
        this.mensagemService = mensagemService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Mensagem>>> getAllMessages() {
        List<Mensagem> mensagens = mensagemService.listarTodas();
        return ResponseEntity.ok(new ApiResponse<>("Mensagens listadas com sucesso.", mensagens));
    }

    // Buscar conversas de um usuário específico
    @GetMapping(params = {"senderId", "senderType"})
    public ResponseEntity<ApiResponse<List<Mensagem>>> getConversations(
            @RequestParam String senderId,
            @RequestParam String senderType) {
        List<Mensagem> conversas = mensagemService.buscarConversasPorUsuario(senderId, senderType);
        return ResponseEntity.ok(new ApiResponse<>("Conversas listadas com sucesso.", conversas));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Mensagem>> sendMessage(@Valid @RequestBody Mensagem mensagem) {
        mensagemService.enviarMensagem(mensagem);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("Mensagem enviada com sucesso.", mensagem));
    }

    // Marcar mensagem como lida
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<Mensagem>> markAsRead(@PathVariable String id) {
        Mensagem mensagem = mensagemService.marcarComoLida(id);
        if (mensagem == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>("Mensagem não encontrada."));
        }
        return ResponseEntity.ok(new ApiResponse<>("Mensagem marcada como lida.", mensagem));
    }
}