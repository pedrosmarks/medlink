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
            Map.of(
                "id", "1",
                "nome", "João da Silva",
                "avatar", "https://cdn-icons-png.flaticon.com/512/921/921347.png",
                "cpf", "123.456.789-10",
                "idade", 30,
                "tipoSanguineo", "A+",
                "telefone", "35 9xxxx-xxxx",
                "email", "joao@exemplo.com.br",
                "observacoes", "",
                "especialistasAutorizados", List.of(1, 2),
                "requisicoesAcesso", List.of(),
                "consultas", List.of(Map.of("data", "2024-06-10", "descricao", "Consulta de rotina")),
                "vacinas", List.of(Map.of("nome", "COVID-19", "data", "2023-01-15")),
                "medicamentos", List.of(Map.of("nome", "Losartana", "dosagem", "50mg")),
                "cirurgias", List.of(Map.of("nome", "Apendicectomia", "data", "2015-08-20")),
                "diagnosticos", List.of(Map.of("nome", "Hipertensão", "data", "2022-03-01")),
                "alergias", List.of(Map.of("descricao", "Nenhuma conhecida"))
            ),
            Map.of(
                "id", "2",
                "nome", "Maria Oliveira",
                "avatar", "https://cdn-icons-png.flaticon.com/512/921/921347.png",
                "cpf", "987.654.321-00",
                "idade", 45,
                "tipoSanguineo", "B-",
                "telefone", "11 9xxxx-xxxx",
                "email", "maria@exemplo.com.br",
                "observacoes", "Paciente diabética",
                "especialistasAutorizados", List.of(1, 2),
                "requisicoesAcesso", List.of(),
                "consultas", List.of(Map.of("data", "2024-05-20", "descricao", "Avaliação de rotina")),
                "vacinas", List.of(Map.of("nome", "Influenza", "data", "2023-03-10")),
                "medicamentos", List.of(Map.of("nome", "Metformina", "dosagem", "850mg")),
                "cirurgias", List.of(),
                "diagnosticos", List.of(Map.of("nome", "Diabetes Tipo 2", "data", "2020-09-15")),
                "alergias", List.of(Map.of("descricao", "Penicilina"))
            )
        );
    }

    @PatchMapping("/{id}")
    public Map<String, Object> updatePaciente(@PathVariable String id, @RequestBody Map<String, Object> changes) {
        // Simula atualização - em implementação real, atualizaria no banco
        return Map.of("success", true, "message", "Paciente atualizado com sucesso");
    }
}