package br.fai.lds.medlink.controller;

import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/relatorios")
@CrossOrigin
public class RelatoriosController {

    @GetMapping
    public List<Map<String, String>> getRelatorios() {
        return List.of(
            Map.of(
                "id", "1",
                "icone", "https://cdn-icons-png.flaticon.com/512/3135/3135715.png",
                "titulo", "Relatório de atendimentos",
                "descricao", "Resumo dos atendimentos realizados no mês."
            ),
            Map.of(
                "id", "2",
                "icone", "https://cdn-icons-png.flaticon.com/512/3135/3135715.png",
                "titulo", "Relatório financeiro",
                "descricao", "Resumo financeiro mensal."
            ),
            Map.of(
                "id", "3",
                "icone", "https://cdn-icons-png.flaticon.com/512/3135/3135715.png",
                "titulo", "Relatório de aniversariantes",
                "descricao", "Lista de pacientes aniversariantes do mês."
            ),
            Map.of(
                "id", "4",
                "icone", "https://cdn-icons-png.flaticon.com/512/3135/3135715.png",
                "titulo", "Relatório por tipo de plano",
                "descricao", "Distribuição de pacientes por tipo de plano."
            )
        );
    }
}