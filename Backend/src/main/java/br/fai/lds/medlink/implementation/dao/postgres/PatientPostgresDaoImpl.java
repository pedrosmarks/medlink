package br.fai.lds.medlink.implementation.dao.postgres;

import br.fai.lds.medlink.domain.*;
import br.fai.lds.medlink.port.dao.patient.PatientDao;

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
        // Mapeia os valores para garantir compatibilidade com o ENUM
        String enumStatus = mapStatusToEnum(status);
        String sql = "UPDATE solicitacao_acesso_prontuario SET status = ?::status_solicitacao WHERE paciente_id = ? AND medico_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, enumStatus);
            ps.setInt(2, patientId);
            ps.setInt(3, medicoId);
            int rows = ps.executeUpdate();
            logger.info("Atualização de status: " + rows + " linha(s) afetada(s) para paciente_id=" + patientId + ", medico_id=" + medicoId + ", novo status=" + enumStatus);
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
        String sql = "INSERT INTO solicitacao_acesso_prontuario (medico_id, paciente_id, status) VALUES (?, ?, 'PENDENTE'::status_solicitacao) ON CONFLICT (medico_id, paciente_id) DO NOTHING";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, medicoId);
            ps.setInt(2, patientId);
            ps.executeUpdate();
        } catch (SQLException e) {
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
            int pessoaId = insertPessoa(entity);
            String sql = "INSERT INTO paciente(pessoa_id, email, senha, convenio_medico, cartao_sus, ativo) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, pessoaId);
            preparedStatement.setString(2, entity.getEmail());
            preparedStatement.setString(3, entity.getPassword());
            preparedStatement.setString(4, entity.getPlan());
            preparedStatement.setString(5, entity.getSusCard());
            preparedStatement.setBoolean(6, entity.isActive());
            preparedStatement.execute();
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
        String sql = "INSERT INTO pessoa(nome, cpf, sexo, data_nascimento) ";
        sql += " VALUES (?, ?, ?, ?)";

        PreparedStatement preparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, entity.getName());
        preparedStatement.setString(2, entity.getCpf());
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
        // Atualiza informações específicas do paciente
        entity.setId(id);
        update(entity);
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
                .requisicoesAcesso(getRequisicoesAcesso(patientId))
                .build();
    }

    private List<RequisicaoAcesso> getRequisicoesAcesso(int patientId) {
        List<RequisicaoAcesso> requisicoes = new ArrayList<>();
        if (patientId <= 0) return requisicoes;
        String sql = "SELECT medico_id, status FROM solicitacao_acesso_prontuario WHERE paciente_id = ? AND status = 'PENDENTE'";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, patientId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                requisicoes.add(new RequisicaoAcesso(rs.getInt("medico_id"), rs.getString("status")));
            }
        } catch (SQLException e) {
            // Log or ignore, return empty list
        }
        return requisicoes;
    }

    private List<EspecialistaAutorizado> getEspecialistasAutorizados(int prontuarioId) {
        List<EspecialistaAutorizado> especialistas = new ArrayList<>();
        if (prontuarioId <= 0) return especialistas;
        String sql = "SELECT medico_id FROM medico_acesso_prontuario WHERE prontuario_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, prontuarioId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                especialistas.add(new EspecialistaAutorizado(rs.getLong("medico_id")));
            }
        } catch (SQLException e) {
            // Log or ignore, return empty list
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
        String sql = "SELECT name, dosage, frequency FROM medicamento WHERE prontuario_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, prontuarioId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                medicamentos.add(Medication.builder()
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
    }
