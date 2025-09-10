package br.fai.lds.medlink.controller;

import br.fai.lds.medlink.domain.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

/**
 * Controlador para dados do dashboard.
 */
@RestController
@RequestMapping("/dashboard")
public class DashboardController extends BaseController {

    /**
     * Retorna dados do dashboard.
     * @return dados estatísticos do dashboard
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<Map<String, String>>>> getDashboard() {
        List<Map<String, String>> dashboardData = List.of(
            Map.of("id", "1", "title", "Novos pacientes", "text", "Você tem 10 novos pacientes.", "size", "col-md-6"),
            Map.of("id", "2", "title", "Consultas agendadas", "text", "Você tem 5 consultas marcadas para hoje.", "size", "col-md-3"),
            Map.of("id", "3", "title", "Mensagens não lidas", "text", "3 mensagens aguardando resposta.", "size", "col-md-3"),
            Map.of("id", "4", "title", "Exames pendentes", "text", "2 exames aguardando análise.", "size", "col-md-4"),
            Map.of("id", "5", "title", "Relatórios disponíveis", "text", "Novos relatórios prontos para revisão.", "size", "col-md-4"),
            Map.of("id", "6", "title", "Solicitações de receita", "text", "1 solicitação de receita pendente.", "size", "col-md-4")
        );
        
        return success("Dados do dashboard carregados com sucesso.", dashboardData);
    }
}