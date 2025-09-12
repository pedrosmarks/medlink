package br.fai.lds.medlink.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/debug")
public class DatabaseCheckController {

    @Autowired
    private javax.sql.DataSource dataSource;
    
    @GetMapping("/test")
    public String test() {
        return "Debug controller funcionando!";
    }
    
    @GetMapping("/simple")
    public String checkSimple() {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = 'public'";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    return "Tabelas encontradas: " + rs.getInt(1);
                }
            }
        } catch (Exception e) {
            return "Erro: " + e.getMessage();
        }
        return "Nenhuma tabela encontrada";
    }

    @GetMapping("/access-requests")
    public Map<String, Object> checkAccessRequests() {
        Map<String, Object> result = new HashMap<>();
        
        try (Connection conn = dataSource.getConnection()) {
            // Verificar solicitações de acesso
            String sql = "SELECT sap.medico_id, sap.paciente_id, sap.status, sap.data_solicitacao, sap.data_resposta, " +
                        "m.nome as medico_nome, p.nome as paciente_nome " +
                        "FROM solicitacao_acesso_prontuario sap " +
                        "JOIN medico med ON sap.medico_id = med.id " +
                        "JOIN pessoa m ON med.pessoa_id = m.id " +
                        "JOIN paciente pac ON sap.paciente_id = pac.id " +
                        "JOIN pessoa p ON pac.pessoa_id = p.id " +
                        "ORDER BY sap.data_solicitacao DESC";
            
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ResultSet rs = ps.executeQuery();
                List<Map<String, Object>> requests = new ArrayList<>();
                
                while (rs.next()) {
                    Map<String, Object> request = new HashMap<>();
                    request.put("medicoId", rs.getInt("medico_id"));
                    request.put("pacienteId", rs.getInt("paciente_id"));
                    request.put("status", rs.getString("status"));
                    request.put("dataSolicitacao", rs.getTimestamp("data_solicitacao"));
                    request.put("dataResposta", rs.getTimestamp("data_resposta"));
                    request.put("medicoNome", rs.getString("medico_nome"));
                    request.put("pacienteNome", rs.getString("paciente_nome"));
                    requests.add(request);
                }
                
                result.put("solicitacoes", requests);
            }
            
            // Verificar acessos diretos
            String sql2 = "SELECT map.medico_id, map.prontuario_id, pr.paciente_id, " +
                         "m.nome as medico_nome, p.nome as paciente_nome " +
                         "FROM medico_acesso_prontuario map " +
                         "JOIN prontuario pr ON map.prontuario_id = pr.id " +
                         "JOIN medico med ON map.medico_id = med.id " +
                         "JOIN pessoa m ON med.pessoa_id = m.id " +
                         "JOIN paciente pac ON pr.paciente_id = pac.id " +
                         "JOIN pessoa p ON pac.pessoa_id = p.id";
            
            try (PreparedStatement ps = conn.prepareStatement(sql2)) {
                ResultSet rs = ps.executeQuery();
                List<Map<String, Object>> accesses = new ArrayList<>();
                
                while (rs.next()) {
                    Map<String, Object> access = new HashMap<>();
                    access.put("medicoId", rs.getInt("medico_id"));
                    access.put("pacienteId", rs.getInt("paciente_id"));
                    access.put("prontuarioId", rs.getInt("prontuario_id"));
                    access.put("medicoNome", rs.getString("medico_nome"));
                    access.put("pacienteNome", rs.getString("paciente_nome"));
                    accesses.add(access);
                }
                
                result.put("acessosDirectos", accesses);
            }
            
        } catch (Exception e) {
            result.put("erro", e.getMessage());
        }
        
        return result;
    }

    @GetMapping("/users")
    public Map<String, Object> checkUsers() {
        Map<String, Object> result = new HashMap<>();
        
        try (Connection conn = dataSource.getConnection()) {
            // Médicos
            String sqlMedicos = "SELECT m.id, p.nome, m.crm, m.especialidade FROM medico m JOIN pessoa p ON m.pessoa_id = p.id";
            try (PreparedStatement ps = conn.prepareStatement(sqlMedicos)) {
                ResultSet rs = ps.executeQuery();
                List<Map<String, Object>> medicos = new ArrayList<>();
                
                while (rs.next()) {
                    Map<String, Object> medico = new HashMap<>();
                    medico.put("id", rs.getInt("id"));
                    medico.put("nome", rs.getString("nome"));
                    medico.put("crm", rs.getString("crm"));
                    medico.put("especialidade", rs.getString("especialidade"));
                    medicos.add(medico);
                }
                result.put("medicos", medicos);
            }
            
            // Pacientes
            String sqlPacientes = "SELECT pac.id, p.nome, pac.email FROM paciente pac JOIN pessoa p ON pac.pessoa_id = p.id WHERE pac.ativo = true";
            try (PreparedStatement ps = conn.prepareStatement(sqlPacientes)) {
                ResultSet rs = ps.executeQuery();
                List<Map<String, Object>> pacientes = new ArrayList<>();
                
                while (rs.next()) {
                    Map<String, Object> paciente = new HashMap<>();
                    paciente.put("id", rs.getInt("id"));
                    paciente.put("nome", rs.getString("nome"));
                    paciente.put("email", rs.getString("email"));
                    pacientes.add(paciente);
                }
                result.put("pacientes", pacientes);
            }
            
        } catch (Exception e) {
            result.put("erro", e.getMessage());
        }
        
        return result;
    }
}