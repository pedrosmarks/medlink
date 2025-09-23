package br.fai.lds.medlink.implementation.dao.postgres;

import br.fai.lds.medlink.domain.*;
import br.fai.lds.medlink.port.dao.patient.PatientDao;
import br.fai.lds.medlink.util.CpfUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

// @Repository
public class PatientPostgresDaoImpl implements PatientDao {
    public void deleteAccessRequest(int patientId, int medicoId) {
        String sql = "DELETE FROM solicitacao_acesso_prontuario WHERE paciente_id = ? AND medico_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, patientId);
            ps.setInt(2, medicoId);
            int rows = ps.executeUpdate();
            logger.info("Requisição apagada: " + rows + " linha(s) para paciente_id=" + patientId + ", medico_id=" + medicoId);
            connection.commit();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erro ao apagar requisição de acesso", e);
        }
    }
    @Override
    public void updateAccessRequestStatus(int patientId, int medicoId, String status) {
        String enumStatus = mapStatusToEnum(status);
        String sql = "UPDATE solicitacao_acesso_prontuario SET status = ?::status_solicitacao, data_resposta = CURRENT_TIMESTAMP WHERE paciente_id = ? AND medico_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, enumStatus);
            ps.setInt(2, patientId);
            ps.setInt(3, medicoId);
            int rows = ps.executeUpdate();
            logger.info("Status atualizado: " + rows + " linha(s) para paciente_id=" + patientId + ", medico_id=" + medicoId + ", status=" + enumStatus);
            connection.commit();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erro ao atualizar status da requisição de acesso", e);
        }
    }

    private String mapStatusToEnum(String status) {
        // Mapeia os status para os valores corretos do ENUM
        if (status == null) return "PENDENTE";
        switch (status.toUpperCase()) {
            case "ACEITA":
            case "APROVADO":
            case "APPROVED":
                return "ACEITA";
            case "RECUSADA":
            case "REJEITADO":
            case "REJECTED":
                return "RECUSADA";
            case "REVOGADA":
            case "REVOKED":
                return "REVOGADA";
            default:
                return "PENDENTE";
        }
    }
    @Override
    public void authorizeSpecialist(int patientId, int medicoId) {
        try {
            int prontuarioId = getProntuarioIdByPatientId(patientId);
            if (prontuarioId > 0) {
                String sql = "INSERT INTO medico_acesso_prontuario (medico_id, prontuario_id) VALUES (?, ?) ON CONFLICT DO NOTHING";
                try (PreparedStatement ps = connection.prepareStatement(sql)) {
                    ps.setInt(1, medicoId);
                    ps.setInt(2, prontuarioId);
                    ps.executeUpdate();
                }
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erro ao autorizar médico", e);
        }
    }
    @Override
    public void createAccessRequest(int patientId, int medicoId) {
        String sql = "INSERT INTO solicitacao_acesso_prontuario (medico_id, paciente_id, status, data_solicitacao) " +
                    "VALUES (?, ?, 'PENDENTE'::status_solicitacao, CURRENT_TIMESTAMP) " +
                    "ON CONFLICT (medico_id, paciente_id) " +
                    "DO UPDATE SET status = 'PENDENTE'::status_solicitacao, data_solicitacao = CURRENT_TIMESTAMP";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, medicoId);
            ps.setInt(2, patientId);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                logger.info("Solicitação criada/atualizada: medico_id=" + medicoId + ", paciente_id=" + patientId);
            }
        } catch (SQLException e) {
            logger.severe("Erro ao criar solicitação de acesso: " + e.getMessage());
            throw new RuntimeException("Erro ao criar requisição de acesso", e);
        }
    }

    private static final Logger logger = Logger.getLogger(PatientPostgresDaoImpl.class.getName());
    private final Connection connection;

    public PatientPostgresDaoImpl(Connection connection) {
        this.connection = connection;
    }

    // Implementação do método exigido pela interface CreateDao
    @Override
    public void create(final Patient entity) {
        logger.log(Level.INFO, "Preparando para adicionar o paciente no banco de dados");
        try {
            connection.setAutoCommit(false);

            // Sincroniza a sequence da tabela paciente para evitar conflitos de chave primária
            try (PreparedStatement syncStmt = connection.prepareStatement(
                "SELECT setval('paciente_id_seq', (SELECT COALESCE(MAX(id), 0) + 1 FROM paciente), false)")) {
                syncStmt.execute();
            } catch (SQLException e) {
                // Se não conseguir sincronizar, continua (pode ser que a sequence já esteja correta)
                logger.warning("Aviso ao sincronizar sequence da paciente: " + e.getMessage());
            }

            int pessoaId = insertPessoa(entity);

            String sql = "INSERT INTO paciente(pessoa_id, email, senha, convenio_medico, cartao_sus, ativo) VALUES (?, ?, ?, ?, ?, ?) RETURNING id";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, pessoaId);
            preparedStatement.setString(2, entity.getEmail());
            preparedStatement.setString(3, entity.getPassword());
            preparedStatement.setString(4, entity.getPlan());
            preparedStatement.setString(5, entity.getSusCard());
            preparedStatement.setBoolean(6, entity.isActive());

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int patientId = resultSet.getInt(1);
                entity.setId(patientId); // Define o ID na entidade

                // Criar prontuário para o paciente
                createProntuario(patientId, entity);
            }

            resultSet.close();
            preparedStatement.close();
            connection.commit();
            logger.log(Level.INFO, "Paciente adicionado com sucesso.");
        } catch (SQLException e) {
            try {
                logger.log(Level.SEVERE, "Problema ao adicionar o paciente no banco de dados. Realizando o rollback.");
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }
    }

    // Método add mantido para compatibilidade, mas remove @Override
    public int add(Patient entity) {
        logger.log(Level.INFO, "Preparando para adicionar o paciente no banco de dados");

        try {
            connection.setAutoCommit(false);

            // Primeiro inserir a pessoa
            int pessoaId = insertPessoa(entity);

            // Depois inserir o paciente
            String sql = "INSERT INTO paciente(pessoa_id, email, senha, convenio_medico, cartao_sus, ativo) ";
            sql += " VALUES (?, ?, ?, ?, ?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, pessoaId);
            preparedStatement.setString(2, entity.getEmail());
            preparedStatement.setString(3, entity.getPassword());
            preparedStatement.setString(4, entity.getPlan());
            preparedStatement.setString(5, entity.getSusCard());
            preparedStatement.setBoolean(6, entity.isActive());

            preparedStatement.execute();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            int id = 0;
            if(resultSet.next()){
                id = resultSet.getInt(1);
            }

            // Criar prontuário para o paciente
            createProntuario(id, entity);

            connection.commit();

            resultSet.close();
            preparedStatement.close();

            logger.log(Level.INFO, "Paciente adicionado com sucesso.");
            return id;

        } catch (SQLException e) {
            try {
                logger.log(Level.SEVERE, "Problema ao adicionar o paciente no banco de dados. Realizando o rollback.");
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }
    }

    private int insertPessoa(Patient entity) throws SQLException {
        // Sincroniza a sequence da tabela pessoa para evitar conflitos de chave primária
        try (PreparedStatement syncStmt = connection.prepareStatement(
            "SELECT setval('pessoa_id_seq', (SELECT COALESCE(MAX(id), 0) + 1 FROM pessoa), false)")) {
            syncStmt.execute();
        } catch (SQLException e) {
            // Se não conseguir sincronizar, continua (pode ser que a sequence já esteja correta)
            logger.warning("Aviso ao sincronizar sequence da pessoa: " + e.getMessage());
        }

        String sql = "INSERT INTO pessoa(nome, cpf, sexo, data_nascimento) ";
        sql += " VALUES (?, ?, ?, ?)";

        PreparedStatement preparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, entity.getName());

        // Remove formatação do CPF usando a classe utilitária
        String cpfSemFormatacao = CpfUtil.removeFormatacao(entity.getCpf());
        preparedStatement.setString(2, cpfSemFormatacao);

        preparedStatement.setString(3, entity.getGender() == Gender.MASCULINO ? "M" : "F");
        preparedStatement.setDate(4, java.sql.Date.valueOf(entity.getBirthDate()));

        preparedStatement.execute();

        ResultSet resultSet = preparedStatement.getGeneratedKeys();
        int id = 0;
        if(resultSet.next()){
            id = resultSet.getInt(1);
        }

        resultSet.close();
        preparedStatement.close();
        return id;
    }

    private void createProntuario(int pacienteId, Patient entity) throws SQLException {
        // Sincroniza a sequence da tabela prontuario para evitar conflitos de chave primária
        try (PreparedStatement syncStmt = connection.prepareStatement(
            "SELECT setval('prontuario_id_seq', (SELECT COALESCE(MAX(id), 0) + 1 FROM prontuario), false)")) {
            syncStmt.execute();
        } catch (SQLException e) {
            // Se não conseguir sincronizar, continua (pode ser que a sequence já esteja correta)
            logger.warning("Aviso ao sincronizar sequence do prontuario: " + e.getMessage());
        }

        String sql = "INSERT INTO prontuario(paciente_id, tipo_sanguineo, doador_orgao, observacoes) ";
        sql += " VALUES (?, ?, ?, ?)";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, pacienteId);
        preparedStatement.setString(2, entity.getBloodType() != null ? entity.getBloodType() : "O+");
        preparedStatement.setBoolean(3, true); // Default para doador
        preparedStatement.setString(4, entity.getObservations());

        preparedStatement.execute();
        preparedStatement.close();
    }

    @Override
    public boolean remove(int id) {
        logger.log(Level.INFO, "Preparando para remover o paciente");
        String sql = "UPDATE paciente SET ativo = false WHERE id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            int rowsAffected = preparedStatement.executeUpdate();
            preparedStatement.close();
            boolean success = rowsAffected > 0;
            if (success) {
                logger.log(Level.INFO, "Paciente removido com sucesso.");
            } else {
                logger.log(Level.WARNING, "Nenhum paciente foi removido - ID não encontrado.");
            }
            return success;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erro ao remover paciente.", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Patient readById(int id) {
        final String sql = """
            SELECT pac.id, pe.nome, pe.cpf, pac.email, pac.senha, pe.sexo, pe.data_nascimento,
                   pac.convenio_medico, pac.cartao_sus, pac.ativo, pr.tipo_sanguineo, pr.observacoes
            FROM paciente pac
            JOIN pessoa pe ON pac.pessoa_id = pe.id
            LEFT JOIN prontuario pr ON pr.paciente_id = pac.id
            WHERE pac.id = ?
        """;

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Patient patient = buildPatientFromResultSet(resultSet);
                preparedStatement.close();
                resultSet.close();
                return patient;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public List<Patient> readAll() {
        final String sql = """
            SELECT pac.id, pe.nome, pe.cpf, pac.email, pac.senha, pe.sexo, pe.data_nascimento,
                   pac.convenio_medico, pac.cartao_sus, pac.ativo, pr.tipo_sanguineo, pr.observacoes
            FROM paciente pac
            JOIN pessoa pe ON pac.pessoa_id = pe.id
            LEFT JOIN prontuario pr ON pr.paciente_id = pac.id
            WHERE pac.ativo = true
        """;

        List<Patient> patients = new ArrayList<>();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                patients.add(buildPatientFromResultSet(resultSet));
            }

            preparedStatement.close();
            resultSet.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return patients;
    }

    public void update(Patient entity) {
        logger.log(Level.INFO, "Preparando para atualizar o paciente");

        try {
            connection.setAutoCommit(false);

            // Atualizar pessoa
            String sqlPessoa = "UPDATE pessoa SET nome = ?, cpf = ?, sexo = ?, data_nascimento = ? WHERE id = (SELECT pessoa_id FROM paciente WHERE id = ?)";
            PreparedStatement preparedStatementPessoa = connection.prepareStatement(sqlPessoa);
            preparedStatementPessoa.setString(1, entity.getName());
            preparedStatementPessoa.setString(2, entity.getCpf());
            preparedStatementPessoa.setString(3, entity.getGender() == Gender.MASCULINO ? "M" : "F");
            preparedStatementPessoa.setDate(4, java.sql.Date.valueOf(entity.getBirthDate()));
            preparedStatementPessoa.setInt(5, entity.getId());
            preparedStatementPessoa.execute();
            preparedStatementPessoa.close();

            // Atualizar paciente
            String sql = "UPDATE paciente SET email = ?, convenio_medico = ?, cartao_sus = ?, ativo = ? WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, entity.getEmail());
            preparedStatement.setString(2, entity.getPlan());
            preparedStatement.setString(3, entity.getSusCard());
            preparedStatement.setBoolean(4, entity.isActive());
            preparedStatement.setInt(5, entity.getId());
            preparedStatement.execute();
            preparedStatement.close();

            connection.commit();
            logger.log(Level.INFO, "Paciente atualizado com sucesso.");

        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateInformation(int id, Patient entity) {
        try {
            connection.setAutoCommit(false);
            
            // Atualiza informações específicas do paciente
            entity.setId(id);
            update(entity);
            
            int prontuarioId = getProntuarioIdByPatientId(id);
            // Atualiza todas as listas médicas no banco
            updateVacinas(id, entity.getVacinas());
            updateAlergias(prontuarioId, entity.getAlergias());
            updateDiagnosticos(prontuarioId, entity.getDiagnosticos());
            updateMedicamentos(prontuarioId, entity.getMedications());
            updateCirurgias(prontuarioId, entity.getCirurgias());
            updateConsultas(prontuarioId, entity.getConsultations());
            
            connection.commit();
            logger.info("Informações do paciente " + id + " atualizadas com sucesso");
            
        } catch (Exception e) {
            try {
                connection.rollback();
                logger.severe("Rollback realizado devido ao erro: " + e.getMessage());
            } catch (SQLException rollbackEx) {
                logger.severe("Erro no rollback: " + rollbackEx.getMessage());
            }
            throw new RuntimeException("Erro ao atualizar dados médicos", e);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                logger.warning("Erro ao restaurar autoCommit: " + e.getMessage());
            }
        }
    }
    
    private void updateVacinas(int patientId, List<Vaccine> vacinas) {
        try {
            // Removes todas as vacinas existentes
            String deleteSql = "DELETE FROM vacina WHERE paciente_id = ?";
            try (PreparedStatement ps = connection.prepareStatement(deleteSql)) {
                ps.setInt(1, patientId);
                ps.executeUpdate();
            }
            
            // Insere as vacinas atualizadas
            if (vacinas != null && !vacinas.isEmpty()) {
                String insertSql = "INSERT INTO vacina (name, date, paciente_id) VALUES (?, ?, ?)";
                try (PreparedStatement ps = connection.prepareStatement(insertSql)) {
                    for (Vaccine vacina : vacinas) {
                        ps.setString(1, vacina.getName());
                        ps.setDate(2, java.sql.Date.valueOf(vacina.getDate()));
                        ps.setInt(3, patientId);
                        ps.addBatch();
                    }
                    ps.executeBatch();
                }
            }
            
            connection.commit();
            logger.info("Vacinas atualizadas com sucesso para paciente " + patientId);
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                logger.severe("Erro no rollback: " + ex.getMessage());
            }
            logger.severe("Erro ao atualizar vacinas: " + e.getMessage());
            throw new RuntimeException("Erro ao atualizar vacinas", e);
        }
    }
    
    private void updateAlergias(int prontuarioId, List<Allergy> alergias) {
        try {
            String deleteSql = "DELETE FROM alergia WHERE prontuario_id = ?";
            try (PreparedStatement ps = connection.prepareStatement(deleteSql)) {
                ps.setInt(1, prontuarioId);
                ps.executeUpdate();
            }
            
            if (alergias != null && !alergias.isEmpty()) {
                String insertSql = "INSERT INTO alergia (id, name, substance, reaction, severity, prontuario_id) VALUES (?, ?, ?, ?, ?, ?)";
                try (PreparedStatement ps = connection.prepareStatement(insertSql)) {
                    for (Allergy alergia : alergias) {
                        ps.setInt(1, alergia.getId());
                        ps.setString(2, alergia.getName());
                        ps.setString(3, alergia.getSubstance());
                        ps.setString(4, alergia.getReaction());
                        ps.setString(5, alergia.getSeverity());
                        ps.setInt(6, prontuarioId);
                        ps.addBatch();
                    }
                    ps.executeBatch();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar alergias", e);
        }
    }
    
    private void updateDiagnosticos(int prontuarioId, List<Diagnosis> diagnosticos) {
        try {
            String deleteSql = "DELETE FROM diagnostico WHERE prontuario_id = ?";
            try (PreparedStatement ps = connection.prepareStatement(deleteSql)) {
                ps.setInt(1, prontuarioId);
                int deleted = ps.executeUpdate();
                logger.info("Diagnósticos deletados: " + deleted + " para prontuario_id=" + prontuarioId);
            }
            
            if (diagnosticos != null && !diagnosticos.isEmpty()) {
                String insertSql = "INSERT INTO diagnostico (description, date, prontuario_id) VALUES (?, ?, ?)";
                try (PreparedStatement ps = connection.prepareStatement(insertSql, PreparedStatement.RETURN_GENERATED_KEYS)) {
                    for (Diagnosis diagnostico : diagnosticos) {
                        ps.setString(1, diagnostico.getDescription());
                        ps.setDate(2, java.sql.Date.valueOf(diagnostico.getDate()));
                        ps.setInt(3, prontuarioId);
                        ps.addBatch();
                    }
                    int[] results = ps.executeBatch();
                    
                    // Recuperar IDs gerados
                    ResultSet generatedKeys = ps.getGeneratedKeys();
                    int index = 0;
                    while (generatedKeys.next() && index < diagnosticos.size()) {
                        diagnosticos.get(index).setId(generatedKeys.getInt(1));
                        index++;
                    }
                    generatedKeys.close();
                    
                    logger.info("Diagnósticos inseridos: " + results.length + " para prontuario_id=" + prontuarioId);
                }
            }
        } catch (SQLException e) {
            logger.severe("Erro ao atualizar diagnósticos: " + e.getMessage());
            throw new RuntimeException("Erro ao atualizar diagnósticos", e);
        }
    }
    
    private void updateMedicamentos(int prontuarioId, List<Medication> medicamentos) {
        try {
            String deleteSql = "DELETE FROM medicamento WHERE prontuario_id = ?";
            try (PreparedStatement ps = connection.prepareStatement(deleteSql)) {
                ps.setInt(1, prontuarioId);
                int deleted = ps.executeUpdate();
                logger.info("Medicamentos deletados: " + deleted + " para prontuario_id=" + prontuarioId);
            }
            
            if (medicamentos != null && !medicamentos.isEmpty()) {
                String insertSql = "INSERT INTO medicamento (name, dosage, frequency, prontuario_id) VALUES (?, ?, ?, ?)";
                try (PreparedStatement ps = connection.prepareStatement(insertSql, PreparedStatement.RETURN_GENERATED_KEYS)) {
                    for (Medication medicamento : medicamentos) {
                        ps.setString(1, medicamento.getName());
                        ps.setString(2, medicamento.getDosage());
                        ps.setString(3, medicamento.getFrequency());
                        ps.setInt(4, prontuarioId);
                        ps.addBatch();
                    }
                    int[] results = ps.executeBatch();
                    
                    // Recuperar IDs gerados
                    ResultSet generatedKeys = ps.getGeneratedKeys();
                    int index = 0;
                    while (generatedKeys.next() && index < medicamentos.size()) {
                        medicamentos.get(index).setId(generatedKeys.getInt(1));
                        index++;
                    }
                    generatedKeys.close();
                    
                    logger.info("Medicamentos inseridos: " + results.length + " para prontuario_id=" + prontuarioId);
                }
            }
        } catch (SQLException e) {
            logger.severe("Erro ao atualizar medicamentos: " + e.getMessage());
            throw new RuntimeException("Erro ao atualizar medicamentos", e);
        }
    }
    
    private void updateCirurgias(int prontuarioId, List<Surgery> cirurgias) {
        try {
            String deleteSql = "DELETE FROM historico_cirurgico WHERE prontuario_id = ?";
            try (PreparedStatement ps = connection.prepareStatement(deleteSql)) {
                ps.setInt(1, prontuarioId);
                ps.executeUpdate();
            }
            
            if (cirurgias != null && !cirurgias.isEmpty()) {
                String insertSql = "INSERT INTO historico_cirurgico (id, descricao_cirurgica, data_cirurgia, prontuario_id) VALUES (?, ?, ?, ?)";
                try (PreparedStatement ps = connection.prepareStatement(insertSql)) {
                    for (Surgery cirurgia : cirurgias) {
                        ps.setInt(1, cirurgia.getId());
                        ps.setString(2, cirurgia.getName());
                        ps.setDate(3, cirurgia.getDate() != null ? java.sql.Date.valueOf(cirurgia.getDate()) : null);
                        ps.setInt(4, prontuarioId);
                        ps.addBatch();
                    }
                    ps.executeBatch();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar cirurgias", e);
        }
    }
    
    private void updateConsultas(int prontuarioId, List<Consultation> consultas) {
        try {
            String deleteSql = "DELETE FROM consulta WHERE prontuario_id = ?";
            try (PreparedStatement ps = connection.prepareStatement(deleteSql)) {
                ps.setInt(1, prontuarioId);
                ps.executeUpdate();
            }
            
            if (consultas != null && !consultas.isEmpty()) {
                String insertSql = "INSERT INTO consulta (data_hora, observacao, prontuario_id) VALUES (?, ?, ?)";
                try (PreparedStatement ps = connection.prepareStatement(insertSql)) {
                    for (Consultation consulta : consultas) {
                        ps.setTimestamp(1, java.sql.Timestamp.valueOf(consulta.getDate().atStartOfDay()));
                        ps.setString(2, consulta.getNotes());
                        ps.setInt(3, prontuarioId);
                        ps.addBatch();
                    }
                    ps.executeBatch();
                }
            }
        } catch (SQLException e) {
            logger.severe("Erro ao atualizar consultas: " + e.getMessage());
            throw new RuntimeException("Erro ao atualizar consultas", e);
        }
    }
    
    private int getDefaultMedicoClinicaEspecialidadeId() {
        try {
            String sql = "SELECT id FROM medico_clinica_especialidade LIMIT 1";
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        } catch (SQLException e) {
            logger.warning("Erro ao buscar medico_clinica_especialidade_id padrão: " + e.getMessage());
        }
        // Se não encontrar nenhum, retorna 1 (assumindo que existe)
        return 1;
    }

    @Override
    public Patient findByEmail(String email) {
        final String sql = """
            SELECT pac.id, pe.nome, pe.cpf, pac.email, pac.senha, pe.sexo, pe.data_nascimento, 
                   pac.convenio_medico, pac.cartao_sus, pac.ativo, pr.tipo_sanguineo, pr.observacoes
            FROM paciente pac
            JOIN pessoa pe ON pac.pessoa_id = pe.id
            LEFT JOIN prontuario pr ON pr.paciente_id = pac.id
            WHERE pac.email = ? AND pac.ativo = true
        """;

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, email);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Patient patient = buildPatientFromResultSet(resultSet);
                preparedStatement.close();
                resultSet.close();
                return patient;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }    @Override
    public List<Patient> findByMedicId(int medicId) {
        // Busca pacientes que aprovaram acesso ao médico através da solicitacao_acesso_prontuario
        final String sql = """
            SELECT DISTINCT pac.id, pe.nome, pe.cpf, pac.email, pac.senha, pe.sexo, pe.data_nascimento,
                   pac.convenio_medico, pac.cartao_sus, pac.ativo, pr.tipo_sanguineo, pr.observacoes
            FROM paciente pac
            JOIN pessoa pe ON pac.pessoa_id = pe.id
            LEFT JOIN prontuario pr ON pr.paciente_id = pac.id
            JOIN solicitacao_acesso_prontuario sap ON sap.paciente_id = pac.id
            WHERE sap.medico_id = ? AND sap.status = 'ACEITA'::status_solicitacao AND pac.ativo = true
        """;

        List<Patient> patients = new ArrayList<>();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, medicId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                patients.add(buildPatientFromResultSet(resultSet));
            }

            preparedStatement.close();
            resultSet.close();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar pacientes do médico " + medicId + ": " + e.getMessage(), e);
        }

        return patients;
    }

    @Override
    public boolean deactivate(int id) {
        // Implementação do método exigido pela interface SoftDeleteDao
        logger.log(Level.INFO, "Desativando paciente com ID: " + id);
        String sql = "UPDATE paciente SET ativo = false WHERE id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            int rowsAffected = preparedStatement.executeUpdate();
            preparedStatement.close();

            boolean success = rowsAffected > 0;
            if (success) {
                logger.log(Level.INFO, "Paciente desativado com sucesso.");
            } else {
                logger.log(Level.WARNING, "Nenhum paciente foi desativado - ID não encontrado.");
            }
            return success;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erro ao desativar paciente.", e);
            throw new RuntimeException(e);
        }
    }

    public List<Patient> findAll() {
        return readAll();
    }

    public void softDelete(int id) {
        remove(id);
    }

    private Patient buildPatientFromResultSet(ResultSet resultSet) throws SQLException {
        int patientId = resultSet.getInt("id");
        int prontuarioId = 0;
        try {
            prontuarioId = getProntuarioIdByPatientId(patientId);
        } catch (Exception e) {
            prontuarioId = 0;
        }
        java.sql.Date birthDateSql = resultSet.getDate("data_nascimento");
        String bloodType = resultSet.getString("tipo_sanguineo");
        String observations = resultSet.getString("observacoes");
        List<RequisicaoAcesso> requisicoes = getRequisicoesAcesso(patientId);
        logger.info("Construindo paciente " + patientId + " com " + requisicoes.size() + " requisições pendentes");
        
        return Patient.builder()
                .id(patientId)
                .name(resultSet.getString("nome"))
                .cpf(resultSet.getString("cpf"))
                .email(resultSet.getString("email"))
                .password(resultSet.getString("senha"))
                .gender("M".equals(resultSet.getString("sexo")) ? Gender.MASCULINO : Gender.FEMININO)
                .birthDate(birthDateSql != null ? birthDateSql.toLocalDate() : null)
                .plan(resultSet.getString("convenio_medico"))
                .susCard(resultSet.getString("cartao_sus"))
                .active(resultSet.getBoolean("ativo"))
                .bloodType(bloodType != null ? bloodType : "")
                .observations(observations != null ? observations : "")
                // Popula listas do banco
                .vacinas(safeGetVacinasByPatientId(patientId))
                .alergias(safeGetAlergiasByProntuarioId(prontuarioId))
                .diagnosticos(safeGetDiagnosticosByProntuarioId(prontuarioId))
                .medications(safeGetMedicamentosByProntuarioId(prontuarioId))
                .cirurgias(safeGetCirurgiasByProntuarioId(prontuarioId))
                .consultations(safeGetConsultasByProntuarioId(prontuarioId))
                // Popula especialistas autorizados
                .especialistasAutorizados(getEspecialistasAutorizados(prontuarioId))
                .requisicoesAcesso(requisicoes)
                .build();
    }

    private List<RequisicaoAcesso> getRequisicoesAcesso(int patientId) {
        List<RequisicaoAcesso> requisicoes = new ArrayList<>();
        if (patientId <= 0) return requisicoes;
        
        // Busca TODAS as solicitações (PENDENTE e ACEITA)
        String sql = "SELECT medico_id, status FROM solicitacao_acesso_prontuario WHERE paciente_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, patientId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String status = rs.getString("status");
                int medicoId = rs.getInt("medico_id");
                requisicoes.add(new RequisicaoAcesso(medicoId, status));
                logger.info("Requisição encontrada: medico_id=" + medicoId + ", status=" + status + " para paciente_id=" + patientId);
            }
            
            logger.info("Total de requisições para paciente " + patientId + ": " + requisicoes.size());
            
        } catch (SQLException e) {
            logger.warning("Erro ao buscar requisições de acesso: " + e.getMessage());
        }
        return requisicoes;
    }

    private List<EspecialistaAutorizado> getEspecialistasAutorizados(int prontuarioId) {
        List<EspecialistaAutorizado> especialistas = new ArrayList<>();
        if (prontuarioId <= 0) return especialistas;
        
        // Busca médicos autorizados através da solicitação aceita
        String sql = """
            SELECT DISTINCT sap.medico_id 
            FROM solicitacao_acesso_prontuario sap
            JOIN prontuario pr ON pr.paciente_id = sap.paciente_id
            WHERE pr.id = ? AND sap.status = 'ACEITA'::status_solicitacao
            UNION
            SELECT medico_id FROM medico_acesso_prontuario WHERE prontuario_id = ?
        """;
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, prontuarioId);
            ps.setInt(2, prontuarioId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                especialistas.add(new EspecialistaAutorizado(rs.getLong("medico_id")));
            }
            logger.info("Especialistas autorizados encontrados: " + especialistas.size() + " para prontuario_id=" + prontuarioId);
        } catch (SQLException e) {
            logger.warning("Erro ao buscar especialistas autorizados: " + e.getMessage());
        }
        return especialistas;
    }

    // Métodos safe para evitar exceções
    private List<Vaccine> safeGetVacinasByPatientId(int patientId) {
        try { return getVacinasByPatientId(patientId); } catch (Exception e) { return new ArrayList<>(); }
    }
    private List<Allergy> safeGetAlergiasByProntuarioId(int prontuarioId) {
        try { return getAlergiasByProntuarioId(prontuarioId); } catch (Exception e) { return new ArrayList<>(); }
    }
    private List<Diagnosis> safeGetDiagnosticosByProntuarioId(int prontuarioId) {
        try { return getDiagnosticosByProntuarioId(prontuarioId); } catch (Exception e) { return new ArrayList<>(); }
    }
    private List<Medication> safeGetMedicamentosByProntuarioId(int prontuarioId) {
        try { return getMedicamentosByProntuarioId(prontuarioId); } catch (Exception e) { return new ArrayList<>(); }
    }
    private List<Surgery> safeGetCirurgiasByProntuarioId(int prontuarioId) {
        try { return getCirurgiasByProntuarioId(prontuarioId); } catch (Exception e) { return new ArrayList<>(); }
    }
    private List<Consultation> safeGetConsultasByProntuarioId(int prontuarioId) {
        try { return getConsultasByProntuarioId(prontuarioId); } catch (Exception e) { return new ArrayList<>(); }
    }

    private int getProntuarioIdByPatientId(int patientId) throws SQLException {
        String sql = "SELECT id FROM prontuario WHERE paciente_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, patientId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        }
        return 0;
    }

    private List<Vaccine> getVacinasByPatientId(int patientId) throws SQLException {
        List<Vaccine> vacinas = new ArrayList<>();
        String sql = "SELECT id, name, date FROM vacina WHERE paciente_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, patientId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                vacinas.add(Vaccine.builder()
                        .id(rs.getInt("id"))
                        .name(rs.getString("name"))
                        .date(rs.getDate("date").toLocalDate())
                        .build());
            }
        }
        return vacinas;
    }

    private List<Allergy> getAlergiasByProntuarioId(int prontuarioId) throws SQLException {
        List<Allergy> alergias = new ArrayList<>();
        String sql = "SELECT id, name, substance, reaction, severity FROM alergia WHERE prontuario_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, prontuarioId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                alergias.add(Allergy.builder()
                        .id(rs.getInt("id"))
                        .name(rs.getString("name"))
                        .substance(rs.getString("substance"))
                        .reaction(rs.getString("reaction"))
                        .severity(rs.getString("severity"))
                        .build());
            }
        }
        return alergias;
    }

    private List<Diagnosis> getDiagnosticosByProntuarioId(int prontuarioId) throws SQLException {
        List<Diagnosis> diagnosticos = new ArrayList<>();
        String sql = "SELECT id, description, date FROM diagnostico WHERE prontuario_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, prontuarioId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                diagnosticos.add(Diagnosis.builder()
                        .id(rs.getInt("id"))
                        .description(rs.getString("description"))
                        .date(rs.getDate("date").toLocalDate())
                        .build());
            }
        }
        return diagnosticos;
    }

    private List<Medication> getMedicamentosByProntuarioId(int prontuarioId) throws SQLException {
        List<Medication> medicamentos = new ArrayList<>();
        String sql = "SELECT id, name, dosage, frequency FROM medicamento WHERE prontuario_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, prontuarioId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                medicamentos.add(Medication.builder()
                        .id(rs.getInt("id"))
                        .name(rs.getString("name"))
                        .dosage(rs.getString("dosage"))
                        .frequency(rs.getString("frequency"))
                        .build());
            }
        }
        return medicamentos;
    }

    private List<Surgery> getCirurgiasByProntuarioId(int prontuarioId) throws SQLException {
        List<Surgery> cirurgias = new ArrayList<>();
        String sql = "SELECT id, descricao_cirurgica, data_cirurgia, '' as location, '' as notes FROM historico_cirurgico WHERE prontuario_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, prontuarioId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                cirurgias.add(Surgery.builder()
                        .id(rs.getInt("id"))
                        .name(rs.getString("descricao_cirurgica"))
                        .date(rs.getDate("data_cirurgia") != null ? rs.getDate("data_cirurgia").toLocalDate() : null)
                        .location(rs.getString("location"))
                        .notes(rs.getString("notes"))
                        .build());
            }
        }
        return cirurgias;
    }

    private List<Consultation> getConsultasByProntuarioId(int prontuarioId) throws SQLException {
        List<Consultation> consultas = new ArrayList<>();
        String sql = "SELECT data_hora, observacao FROM consulta WHERE prontuario_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, prontuarioId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                consultas.add(Consultation.builder()
                        .date(rs.getTimestamp("data_hora").toLocalDateTime().toLocalDate())
                        .reason(rs.getString("observacao"))
                        .notes(rs.getString("observacao"))
                        .build());
            }
        }
        return consultas;
    }

    // Revoga o acesso de um médico ao prontuário do paciente
    public void revokeAccess(int patientId, int medicoId) {
        try {
            // Atualiza o status da solicitação para REVOGADA
            String sqlUpdate = "UPDATE solicitacao_acesso_prontuario SET status = 'REVOGADA'::status_solicitacao, data_resposta = CURRENT_TIMESTAMP WHERE paciente_id = ? AND medico_id = ?";
            try (PreparedStatement ps = connection.prepareStatement(sqlUpdate)) {
                ps.setInt(1, patientId);
                ps.setInt(2, medicoId);
                ps.executeUpdate();
            }
            // Remove o vínculo do médico com o prontuário
            int prontuarioId = getProntuarioIdByPatientId(patientId);
            if (prontuarioId > 0) {
                String sqlDelete = "DELETE FROM medico_acesso_prontuario WHERE prontuario_id = ? AND medico_id = ?";
                try (PreparedStatement ps = connection.prepareStatement(sqlDelete)) {
                    ps.setInt(1, prontuarioId);
                    ps.setInt(2, medicoId);
                    ps.executeUpdate();
                }
            }
            logger.info("Acesso revogado: paciente_id=" + patientId + ", medico_id=" + medicoId);
        } catch (SQLException e) {
            logger.severe("Erro ao revogar acesso: " + e.getMessage());
            throw new RuntimeException("Erro ao revogar acesso", e);
        }
    }

    @Override
    public List<Patient> findAuthorizedByMedicId(int medicId) {
        List<Patient> patients = new ArrayList<>();
        final String sql = """
            SELECT pac.id, pe.nome, pe.cpf, pac.email, pac.senha, pe.sexo, pe.data_nascimento,
                   pac.convenio_medico, pac.cartao_sus, pac.ativo, pr.tipo_sanguineo, pr.observacoes
            FROM paciente pac
            JOIN pessoa pe ON pac.pessoa_id = pe.id
            LEFT JOIN prontuario pr ON pr.paciente_id = pac.id
            JOIN solicitacao_acesso_prontuario sap ON sap.paciente_id = pac.id
            WHERE sap.medico_id = ? AND sap.status = 'ACEITA'::status_solicitacao AND pac.ativo = true
        """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, medicId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Patient patient = buildPatientFromResultSet(rs);
                patients.add(patient);
            }
            rs.close();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erro ao buscar pacientes autorizados para o médico: " + medicId, e);
        }
        return patients;
    }

    // Método utilitário para mapear ResultSet para Patient (ajuste conforme já existente)
    private Patient mapResultSetToPatient(ResultSet rs) throws SQLException {
        // Exemplo simplificado, ajuste conforme necessário
        Patient patient = new Patient();
        patient.setId(rs.getInt("id"));
        patient.setName(rs.getString("nome"));
        patient.setEmail(rs.getString("email"));
        patient.setCpf(rs.getString("cpf"));
        patient.setBirthDate(rs.getDate("data_nascimento").toLocalDate());
        // Adicione outros campos conforme necessário
        return patient;
    }

    @Override
    public Consultation addConsultation(int patientId, Consultation consultation) {
        // Buscar o prontuario_id do paciente
        int prontuarioId = -1;
        String prontuarioSql = "SELECT id FROM prontuario WHERE paciente_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(prontuarioSql)) {
            ps.setInt(1, patientId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                prontuarioId = rs.getInt("id");
            }
            rs.close();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erro ao buscar prontuario_id", e);
            throw new RuntimeException("Erro ao buscar prontuario_id", e);
        }
        if (prontuarioId == -1) {
            throw new RuntimeException("Prontuário não encontrado para paciente_id=" + patientId);
        }
        // Inserir consulta usando prontuario_id
        String sql = "INSERT INTO consulta (prontuario_id, data_hora, observacao) VALUES (?, ?, ?) RETURNING id";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, prontuarioId);
            stmt.setTimestamp(2, java.sql.Timestamp.valueOf(consultation.getDate().atStartOfDay()));
            stmt.setString(3, consultation.getNotes());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                consultation.setId(id);
            }
            rs.close();
            return consultation;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erro ao adicionar consulta", e);
            throw new RuntimeException("Erro ao adicionar consulta", e);
        }
    }

    @Override
    public List<Consultation> getConsultationsByPatientId(int patientId) {
        String sql = "SELECT c.id, c.data_hora, c.observacao FROM consulta c " +
                "JOIN prontuario p ON c.prontuario_id = p.id " +
                "WHERE p.paciente_id = ? ORDER BY c.data_hora DESC";
        List<Consultation> consultations = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, patientId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Consultation c = Consultation.builder()
                        .id(rs.getInt("id"))
                        .date(rs.getTimestamp("data_hora").toLocalDateTime().toLocalDate())
                        .reason(rs.getString("observacao"))
                        .notes(rs.getString("observacao"))
                        .build();
                consultations.add(c);
            }
            rs.close();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erro ao buscar consultas", e);
            throw new RuntimeException("Erro ao buscar consultas", e);
        }
        return consultations;
    }

    /**
     * Remove uma consulta do paciente (DELETE direto no banco)
     */
    public boolean deleteConsultation(int patientId, int consultationId) {
        try {
            int prontuarioId = getProntuarioIdByPatientId(patientId);
            if (prontuarioId <= 0) {
                logger.warning("Prontuário não encontrado para paciente " + patientId);
                return false;
            }
            String sql = "DELETE FROM consulta WHERE id = ? AND prontuario_id = ?";
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, consultationId);
                ps.setInt(2, prontuarioId);
                int rows = ps.executeUpdate();
                logger.info("Consultas removidas: " + rows + " para paciente_id=" + patientId + ", consulta_id=" + consultationId);
                return rows > 0;
            }
        } catch (Exception e) {
            logger.severe("Erro ao remover consulta: " + e.getMessage());
            return false;
        }
    }
}
