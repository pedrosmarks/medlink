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
            Map.of(
                "id", "1",
                "nome", "Dr. Pedro Almeida",
                "avatar", "https://cdn-icons-png.flaticon.com/512/3135/3135715.png",
                "especialidade", "Cardiologia",
                "crm", "123456-SP",
                "descricao", "Médico cardiologista com mais de 15 anos de experiência.",
                "cpf", "123.456.789-00",
                "idade", 40,
                "tipoSanguineo", "O+",
                "telefone", "(11) 99999-8888",
                "email", "pedro.almeida@medlink.com",
                "observacoes", "Atende convênios e particular."
            )
        );
    }

    @GetMapping("/{id}")
    public Map<String, Object> getPerfilById(@PathVariable String id) {
        return Map.of(
            "id", id,
            "nome", "Dr. Pedro Almeida",
            "avatar", "https://cdn-icons-png.flaticon.com/512/3135/3135715.png",
            "especialidade", "Cardiologia",
            "crm", "123456-SP",
            "descricao", "Médico cardiologista com mais de 15 anos de experiência.",
            "cpf", "123.456.789-00",
            "idade", 40,
            "tipoSanguineo", "O+",
            "telefone", "(11) 99999-8888",
            "email", "pedro.almeida@medlink.com",
            "observacoes", "Atende convênios e particular."
        );
    }
}