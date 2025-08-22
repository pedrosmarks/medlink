package br.fai.lds.medlink.controller;

import br.fai.lds.medlink.domain.ApiResponse;
import br.fai.lds.medlink.domain.Mensagem;
import br.fai.lds.medlink.implementation.service.mensagem.MensagemServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/mensagens")
@CrossOrigin
public class MensagemController {

    private final MensagemServiceImpl mensagemService;

    public MensagemController(MensagemServiceImpl mensagemService) {
        this.mensagemService = mensagemService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Mensagem>>> getMensagens() {
        List<Mensagem> mensagens = mensagemService.listarTodas();
        return ResponseEntity.ok(new ApiResponse<>("Mensagens listadas com sucesso.", mensagens));
    }

    // Buscar conversas de um usuário específico
    @GetMapping(params = {"remetenteId", "remetenteTipo"})
    public ResponseEntity<ApiResponse<List<Mensagem>>> getConversas(
            @RequestParam String remetenteId,
            @RequestParam String remetenteTipo) {
        List<Mensagem> conversas = mensagemService.buscarConversasPorUsuario(remetenteId, remetenteTipo);
        return ResponseEntity.ok(new ApiResponse<>("Conversas listadas com sucesso.", conversas));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Mensagem>> enviarMensagem(@Valid @RequestBody Mensagem mensagem) {
        mensagemService.enviarMensagem(mensagem);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("Mensagem enviada com sucesso.", mensagem));
    }

    // Marcar mensagem como lida
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<Mensagem>> marcarComoLida(@PathVariable String id) {
        Mensagem mensagem = mensagemService.marcarComoLida(id);
        if (mensagem == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>("Mensagem não encontrada."));
        }
        return ResponseEntity.ok(new ApiResponse<>("Mensagem marcada como lida.", mensagem));
    }
}