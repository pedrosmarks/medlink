package br.fai.lds.medlink.controller;

import br.fai.lds.medlink.domain.ApiResponse;
import br.fai.lds.medlink.domain.Message;
import br.fai.lds.medlink.domain.Patient;
import br.fai.lds.medlink.port.service.message.MessageService;
import br.fai.lds.medlink.port.service.patient.PatientService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Controlador para gerenciamento de mensagens.
 */
@Slf4j
@RestController
@RequestMapping("/api/messages")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"},
           methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PATCH})
public class MessageController extends BaseController {

    private final MessageService messageService;
    private final PatientService patientService;

    public MessageController(MessageService messageService, PatientService patientService) {
        this.messageService = messageService;
        this.patientService = patientService;
    }

    /**
     * Lista todas as mensagens.
     * @return lista de mensagens
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<Message>>> getAllMessages() {
        try {
            List<Message> messages = messageService.findAll();
            return success("Mensagens listadas com sucesso.", messages);
        } catch (Exception e) {
            log.error("Erro ao carregar mensagens: {}", e.getMessage(), e);
            return error("Erro ao carregar mensagens: " + e.getMessage());
        }
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
        
        if (senderId == null || senderId.trim().isEmpty()) {
            return badRequest("SenderId é obrigatório.");
        }
        if (senderType == null || senderType.trim().isEmpty()) {
            return badRequest("SenderType é obrigatório.");
        }
        
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
        
        if (!canSendMessage(message.getSenderId(), message.getRecipientId(), message.getSenderType())) {
            return forbidden("Você não tem autorização para enviar mensagem a este paciente.");
        }
        
        messageService.sendMessage(message);
        return created("Mensagem enviada com sucesso.", message);
    }

    /**
     * Marca mensagem como lida.
     * @param id identificador da mensagem
     * @return dados da mensagem atualizada
     */
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<Message>> markAsRead(@PathVariable String id) {
        
        if (id == null || id.trim().isEmpty()) {
            return badRequest("ID da mensagem é obrigatório.");
        }
        
        Message message = messageService.markAsRead(id);
        return message != null ? 
            success("Mensagem marcada como lida.", message) : 
            notFound("Mensagem");
    }

    /**
     * Verifica se o remetente tem autorização para enviar mensagem ao destinatário.
     * @param senderId ID do remetente
     * @param recipientId ID do destinatário
     * @param senderType tipo do remetente (MEDIC ou PATIENT)
     * @return true se autorizado, false caso contrário
     */
    private boolean canSendMessage(String senderId, String recipientId, String senderType) {
        if (!"MEDIC".equals(senderType)) return true; // Pacientes podem enviar para seus médicos
        
        try {
            List<Patient> patients = patientService.findByMedicId(Integer.parseInt(senderId));
            return patients.stream().anyMatch(p -> p.getId() == Integer.parseInt(recipientId));
        } catch (NumberFormatException e) {
            log.error("Erro ao converter IDs para validação: senderId={}, recipientId={}", senderId, recipientId);
            return false;
        }
    }
}