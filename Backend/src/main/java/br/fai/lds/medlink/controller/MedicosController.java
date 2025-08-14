package br.fai.lds.medlink.controller;

import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/medicos")
@CrossOrigin
public class MedicosController {

    @GetMapping
    public List<Map<String, Object>> getMedicos(@RequestParam(required = false) String usuario, 
                                                @RequestParam(required = false) String senha) {
        // Se tem parâmetros de login, simula autenticação
        if (usuario != null && senha != null) {
            if ("admin".equals(usuario) && "123".equals(senha)) {
                return List.of(Map.of(
                    "id", "1",
                    "nome", "Dr. Pedro Almeida",
                    "usuario", "admin",
                    "email", "pedro.almeida@medlink.com"
                ));
            }
            return List.of(); // Login inválido
        }
        
        // Retorna lista completa
        return List.of(
            Map.of(
                "id", "1",
                "nome", "Dr. Pedro Almeida",
                "especialidade", "Cardiologia",
                "crm", "123456-SP",
                "email", "pedro.almeida@medlink.com",
                "telefone", "(11) 99999-8888"
            ),
            Map.of(
                "id", "2",
                "nome", "Dr. José Silva",
                "especialidade", "Neurologia",
                "crm", "654321-SP",
                "email", "jose.silva@medlink.com",
                "telefone", "(11) 88888-7777"
            )
        );
    }
}