package br.fai.lds.medlink.controller;

import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/pacientes")
@CrossOrigin
public class PacientesController {

    @GetMapping
    public List<Map<String, Object>> getPacientes() {
        return List.of(
            Map.ofEntries(
                Map.entry("id", "1"),
                Map.entry("nome", "João da Silva"),
                Map.entry("avatar", "https://cdn-icons-png.flaticon.com/512/921/921347.png"),
                Map.entry("cpf", "123.456.789-10"),
                Map.entry("idade", 30),
                Map.entry("tipoSanguineo", "A+"),
                Map.entry("telefone", "35 9xxxx-xxxx"),
                Map.entry("email", "joao@exemplo.com.br"),
                Map.entry("observacoes", ""),
                Map.entry("especialistasAutorizados", List.of(1, 2)),
                Map.entry("requisicoesAcesso", List.of()),
                Map.entry("consultas", List.of(Map.of("data", "2024-06-10", "descricao", "Consulta de rotina"))),
                Map.entry("vacinas", List.of(Map.of("nome", "COVID-19", "data", "2023-01-15"))),
                Map.entry("medicamentos", List.of(Map.of("nome", "Losartana", "dosagem", "50mg"))),
                Map.entry("cirurgias", List.of(Map.of("nome", "Apendicectomia", "data", "2015-08-20"))),
                Map.entry("diagnosticos", List.of(Map.of("nome", "Hipertensão", "data", "2022-03-01"))),
                Map.entry("alergias", List.of(Map.of("descricao", "Nenhuma conhecida")))
            ),
            Map.ofEntries(
                Map.entry("id", "2"),
                Map.entry("nome", "Maria Oliveira"),
                Map.entry("avatar", "https://cdn-icons-png.flaticon.com/512/921/921347.png"),
                Map.entry("cpf", "987.654.321-00"),
                Map.entry("idade", 45),
                Map.entry("tipoSanguineo", "B-"),
                Map.entry("telefone", "11 9xxxx-xxxx"),
                Map.entry("email", "maria@exemplo.com.br"),
                Map.entry("observacoes", "Paciente diabética"),
                Map.entry("especialistasAutorizados", List.of(1, 2)),
                Map.entry("requisicoesAcesso", List.of()),
                Map.entry("consultas", List.of(Map.of("data", "2024-05-20", "descricao", "Avaliação de rotina"))),
                Map.entry("vacinas", List.of(Map.of("nome", "Influenza", "data", "2023-03-10"))),
                Map.entry("medicamentos", List.of(Map.of("nome", "Metformina", "dosagem", "850mg"))),
                Map.entry("cirurgias", List.of()),
                Map.entry("diagnosticos", List.of(Map.of("nome", "Diabetes Tipo 2", "data", "2020-09-15"))),
                Map.entry("alergias", List.of(Map.of("descricao", "Penicilina")))
            )
        );
    }

    @PatchMapping("/{id}")
    public Map<String, Object> updatePaciente(@PathVariable String id, @RequestBody Map<String, Object> changes) {
        // Simula atualização - em implementação real, atualizaria no banco
        return Map.of("success", true, "message", "Paciente atualizado com sucesso");
    }
}