package br.fai.lds.medlink.controller;

import br.fai.lds.medlink.domain.Mensagem;
import br.fai.lds.medlink.implementation.service.mensagem.MensagemServiceImpl;
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
    public List<Mensagem> getMensagens() {
        return mensagemService.listarTodas();
    }

    // Buscar conversas de um usuário específico
    @GetMapping(params = {"remetenteId", "remetenteTipo"})
    public List<Mensagem> getConversas(
            @RequestParam String remetenteId,
            @RequestParam String remetenteTipo) {
        return mensagemService.listarTodas().stream()
                .filter(m -> m.getRemetenteId().equals(remetenteId) && m.getRemetenteTipo().equals(remetenteTipo))
                .toList();
    }

    @PostMapping
    public Mensagem enviarMensagem(@RequestBody Mensagem mensagem) {
        mensagemService.enviarMensagem(mensagem);
        return mensagem;
    }

    // Marcar mensagem como lida
    @PatchMapping("/{id}")
    public Mensagem marcarComoLida(@PathVariable String id) {
        return mensagemService.marcarComoLida(id);
    }
}