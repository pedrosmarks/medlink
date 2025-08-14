package br.fai.lds.medlink.controller;

import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/perfil")
@CrossOrigin
public class PerfilController {

    @GetMapping
    public List<Map<String, Object>> getPerfis() {
        return List.of(
            Map.ofEntries(
                Map.entry("id", "1"),
                Map.entry("nome", "Dr. Pedro Almeida"),
                Map.entry("avatar", "https://cdn-icons-png.flaticon.com/512/3135/3135715.png"),
                Map.entry("especialidade", "Cardiologia"),
                Map.entry("crm", "123456-SP"),
                Map.entry("descricao", "Médico cardiologista com mais de 15 anos de experiência."),
                Map.entry("cpf", "123.456.789-00"),
                Map.entry("idade", 40),
                Map.entry("tipoSanguineo", "O+"),
                Map.entry("telefone", "(11) 99999-8888"),
                Map.entry("email", "pedro.almeida@medlink.com"),
                Map.entry("observacoes", "Atende convênios e particular.")
            )
        );
    }

    @GetMapping("/{id}")
    public Map<String, Object> getPerfilById(@PathVariable String id) {
        return Map.ofEntries(
            Map.entry("id", id),
            Map.entry("nome", "Dr. Pedro Almeida"),
            Map.entry("avatar", "https://cdn-icons-png.flaticon.com/512/3135/3135715.png"),
            Map.entry("especialidade", "Cardiologia"),
            Map.entry("crm", "123456-SP"),
            Map.entry("descricao", "Médico cardiologista com mais de 15 anos de experiência."),
            Map.entry("cpf", "123.456.789-00"),
            Map.entry("idade", 40),
            Map.entry("tipoSanguineo", "O+"),
            Map.entry("telefone", "(11) 99999-8888"),
            Map.entry("email", "pedro.almeida@medlink.com"),
            Map.entry("observacoes", "Atende convênios e particular.")
        );
    }
}