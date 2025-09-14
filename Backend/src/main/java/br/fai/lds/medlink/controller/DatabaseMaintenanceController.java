package br.fai.lds.medlink.controller;

import br.fai.lds.medlink.domain.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Controlador para manutenção do banco de dados
 */
@Slf4j
@RestController
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001", "http://localhost:4200"})
@RequestMapping("/api/database")
public class DatabaseMaintenanceController {

    @Autowired
    private DataSource dataSource;

    /**
     * Corrige as sequências do PostgreSQL para resolver conflitos de chave primária
     */
    @PostMapping("/fix-sequences")
    public ResponseEntity<ApiResponse<Map<String, Object>>> fixSequences() {
        log.info("Iniciando correção das sequências do banco de dados");

        Map<String, Object> results = new HashMap<>();

        try (Connection connection = dataSource.getConnection()) {
            // Lista de tabelas e suas sequências
            String[] sequences = {
                "pessoa_id_seq", "endereco_id_seq", "cidade_id_seq",
                "estado_id_seq", "medico_id_seq", "paciente_id_seq",
                "prontuario_id_seq", "especialidade_id_seq", "clinica_id_seq"
            };

            String[] tables = {
                "pessoa", "endereco", "cidade",
                "estado", "medico", "paciente",
                "prontuario", "especialidade", "clinica"
            };

            for (int i = 0; i < sequences.length && i < tables.length; i++) {
                String sequenceName = sequences[i];
                String tableName = tables[i];

                try {
                    // Obter o valor máximo atual da tabela
                    String maxValueSql = "SELECT COALESCE(MAX(id), 0) FROM " + tableName;
                    PreparedStatement maxStmt = connection.prepareStatement(maxValueSql);
                    ResultSet maxRs = maxStmt.executeQuery();

                    int maxValue = 0;
                    if (maxRs.next()) {
                        maxValue = maxRs.getInt(1);
                    }
                    maxRs.close();
                    maxStmt.close();

                    // Definir o próximo valor da sequência
                    String setSeqSql = "SELECT setval('" + sequenceName + "', " + (maxValue + 1) + ")";
                    PreparedStatement seqStmt = connection.prepareStatement(setSeqSql);
                    ResultSet seqRs = seqStmt.executeQuery();

                    int nextValue = 0;
                    if (seqRs.next()) {
                        nextValue = seqRs.getInt(1);
                    }
                    seqRs.close();
                    seqStmt.close();

                    results.put(tableName, Map.of(
                        "maxId", maxValue,
                        "nextSequenceValue", nextValue,
                        "status", "corrigida"
                    ));

                    log.info("Sequência {} corrigida: max_id={}, next_value={}",
                            sequenceName, maxValue, nextValue);

                } catch (SQLException e) {
                    log.warn("Erro ao corrigir sequência {}: {}", sequenceName, e.getMessage());
                    results.put(tableName, Map.of(
                        "status", "erro",
                        "message", e.getMessage()
                    ));
                }
            }

            log.info("Correção das sequências concluída");
            return ResponseEntity.ok(new ApiResponse<>("Sequências corrigidas com sucesso!", results));

        } catch (SQLException e) {
            log.error("Erro ao conectar com o banco de dados: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError()
                    .body(new ApiResponse<>("Erro ao corrigir sequências: " + e.getMessage()));
        }
    }

    /**
     * Verifica o status atual das sequências
     */
    @GetMapping("/sequences-status")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getSequencesStatus() {
        log.info("Verificando status das sequências");

        Map<String, Object> status = new HashMap<>();

        try (Connection connection = dataSource.getConnection()) {
            String sql = """
                SELECT 
                    sequencename, 
                    last_value,
                    increment_by
                FROM pg_sequences 
                WHERE schemaname = 'public'
                ORDER BY sequencename
                """;

            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String seqName = rs.getString("sequencename");
                int lastValue = rs.getInt("last_value");
                int increment = rs.getInt("increment_by");

                status.put(seqName, Map.of(
                    "lastValue", lastValue,
                    "increment", increment
                ));
            }

            rs.close();
            stmt.close();

            return ResponseEntity.ok(new ApiResponse<>("Status das sequências obtido com sucesso!", status));

        } catch (SQLException e) {
            log.error("Erro ao verificar sequências: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError()
                    .body(new ApiResponse<>("Erro ao verificar sequências: " + e.getMessage()));
        }
    }
}
