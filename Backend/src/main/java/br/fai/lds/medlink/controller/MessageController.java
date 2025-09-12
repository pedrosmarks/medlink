package br.fai.lds.medlink.controller;

import br.fai.lds.medlink.domain.ApiResponse;
import br.fai.lds.medlink.domain.Message;
import br.fai.lds.medlink.port.service.message.MessageService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Controlador para gerenciamento de mensagens.
 */
@RestController
@RequestMapping("/api/messages")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"}, 
           methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PATCH})
public class MessageController extends BaseController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    /**
     * Lista todas as mensagens.
     * @return lista de mensagens
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<Message>>> getAllMessages() {
        List<Message> messages = messageService.findAll();
        return success("Mensagens listadas com sucesso.", messages);
    }

    /**
     * Busca conversas por usuário.
     * @param senderId identificador do remetente
     * @param senderType tipo do remetente
     * @return lista de conversas
     */
    @GetMapping(params = {"senderId", "senderType"})
    public ResponseEntity<ApiResponse<List<Message>>> getConversations(
            @RequestParam String senderId,
            @RequestParam String senderType) {
        List<Message> conversations = messageService.findConversationsByUser(senderId, senderType);
        return success("Conversas listadas com sucesso.", conversations);
    }

    /**
     * Envia uma nova mensagem.
     * @param message dados da mensagem
     * @return confirmação do envio
     */
    @PostMapping
    public ResponseEntity<ApiResponse<Message>> sendMessage(@Valid @RequestBody Message message) {
        messageService.sendMessage(message);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>("Mensagem enviada com sucesso.", message));
    }

    /**
     * Marca mensagem como lida.
     * @param id identificador da mensagem
     * @return dados da mensagem atualizada
     */
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<Message>> markAsRead(@PathVariable String id) {
        Message message = messageService.markAsRead(id);
        return message != null ? 
            success("Mensagem marcada como lida.", message) : 
            notFound("Mensagem");
    }
}