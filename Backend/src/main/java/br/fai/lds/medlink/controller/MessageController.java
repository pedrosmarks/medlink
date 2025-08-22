package br.fai.lds.medlink.controller;

import br.fai.lds.medlink.domain.ApiResponse;
import br.fai.lds.medlink.domain.Mensagem;
import br.fai.lds.medlink.port.service.message.MessageService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/messages")
@CrossOrigin
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Mensagem>>> getAllMessages() {
        List<Mensagem> messages = messageService.findAll();
        return ResponseEntity.ok(new ApiResponse<>("Mensagens listadas com sucesso.", messages));
    }

    // Get conversations by user
    @GetMapping(params = {"senderId", "senderType"})
    public ResponseEntity<ApiResponse<List<Mensagem>>> getConversations(
            @RequestParam String senderId,
            @RequestParam String senderType) {
        List<Mensagem> conversations = messageService.findConversationsByUser(senderId, senderType);
        return ResponseEntity.ok(new ApiResponse<>("Conversas listadas com sucesso.", conversations));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Mensagem>> sendMessage(@Valid @RequestBody Mensagem message) {
        messageService.sendMessage(message);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("Mensagem enviada com sucesso.", message));
    }

    // Mark message as read
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<Mensagem>> markAsRead(@PathVariable String id) {
        Mensagem message = messageService.markAsRead(id);
        if (message == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>("Mensagem não encontrada."));
        }
        return ResponseEntity.ok(new ApiResponse<>("Mensagem marcada como lida.", message));
    }
}