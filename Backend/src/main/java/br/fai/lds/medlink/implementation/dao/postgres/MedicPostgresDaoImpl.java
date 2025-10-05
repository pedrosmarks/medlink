package br.fai.lds.medlink.implementation.dao.postgres;

import br.fai.lds.medlink.domain.Gender;
import br.fai.lds.medlink.domain.Medic;
import br.fai.lds.medlink.port.dao.medic.MedicDao;
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
public class MedicPostgresDaoImpl implements MedicDao {

    private static final Logger logger = Logger.getLogger(MedicPostgresDaoImpl.class.getName());
    private final Connection connection;

    public MedicPostgresDaoImpl(Connection connection) {
        this.connection = connection;
    }

    // Implementação do método exigido pela interface CreateDao
    @Override
    public void create(final Medic entity) {
        logger.log(Level.INFO, "Preparando para adicionar o médico no banco de dados");
        try {
            connection.setAutoCommit(false);
            int pessoaId = insertPessoa(entity);

            // Corrigir sequência da tabela medico antes da inserção
            try {
                String fixMedicoSeqSql = "SELECT setval('medico_id_seq', (SELECT COALESCE(MAX(id), 0) + 1 FROM medico), false)";
                PreparedStatement fixMedicoStmt = connection.prepareStatement(fixMedicoSeqSql);
                fixMedicoStmt.execute();
                fixMedicoStmt.close();
                logger.log(Level.INFO, "Sequência medico_id_seq verificada e corrigida se necessário");
            } catch (SQLException e) {
                logger.log(Level.WARNING, "Não foi possível corrigir a sequência medico_id_seq: " + e.getMessage());
            }

            String sql = "INSERT INTO medico(pessoa_id, email, senha, crm, ativo) VALUES (?, ?, crypt(?, gen_salt('bf')), ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, pessoaId);
            preparedStatement.setString(2, entity.getEmail());
            preparedStatement.setString(3, entity.getPassword());
            preparedStatement.setString(4, entity.getCrm());
            preparedStatement.setBoolean(5, entity.isActive());
            preparedStatement.execute();

            // Obter o ID gerado para o médico
            ResultSet medicoKeys = preparedStatement.getGeneratedKeys();
            if (medicoKeys.next()) {
                int medicoId = medicoKeys.getInt(1);
                entity.setId(medicoId);
                logger.log(Level.INFO, "Médico inserido com ID gerado automaticamente: " + medicoId);
            }
            medicoKeys.close();
            preparedStatement.close();

            connection.commit();
            logger.log(Level.INFO, "Médico adicionado com sucesso.");
        } catch (SQLException e) {
            try {
                logger.log(Level.SEVERE, "Problema ao adicionar o médico no banco de dados. Realizando o rollback.");
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }
    }

    private int insertPessoa(Medic entity) throws SQLException {
        // Primeiro, garantir que a sequência esteja correta
        try {
            String fixSequenceSql = "SELECT setval('pessoa_id_seq', (SELECT COALESCE(MAX(id), 0) + 1 FROM pessoa), false)";
            PreparedStatement fixStmt = connection.prepareStatement(fixSequenceSql);
            fixStmt.execute();
            fixStmt.close();
            logger.log(Level.INFO, "Sequência pessoa_id_seq verificada e corrigida se necessário");
        } catch (SQLException e) {
            logger.log(Level.WARNING, "Não foi possível corrigir a sequência, tentando inserção normal: " + e.getMessage());
        }

        String sql = "INSERT INTO pessoa(nome, cpf, sexo, data_nascimento) VALUES (?, ?, ?, ?)";

        PreparedStatement preparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, entity.getName());

        // Remove formatação do CPF antes de inserir no banco
        String cpfSemFormatacao = CpfUtil.removeFormatacao(entity.getCpf());
        preparedStatement.setString(2, cpfSemFormatacao);

        preparedStatement.setString(3, entity.getGender() == Gender.MASCULINO ? "M" : "F");
        preparedStatement.setDate(4, java.sql.Date.valueOf(entity.getBirthDate()));

        preparedStatement.execute();

        ResultSet resultSet = preparedStatement.getGeneratedKeys();
        int id = 0;
        if(resultSet.next()){
            id = resultSet.getInt(1);
            logger.log(Level.INFO, "Pessoa inserida com ID gerado automaticamente: " + id);
        }

        resultSet.close();
        preparedStatement.close();
        return id;
    }

    @Override
    public boolean remove(int id) {
        logger.log(Level.INFO, "Preparando para remover o médico");
        String sql = "UPDATE medico SET ativo = false WHERE id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            int rowsAffected = preparedStatement.executeUpdate();
            preparedStatement.close();
            boolean success = rowsAffected > 0;
            if (success) {
                logger.log(Level.INFO, "Médico removido com sucesso.");
            } else {
                logger.log(Level.WARNING, "Nenhum médico foi removido - ID não encontrado.");
            }
            return success;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erro ao remover médico.", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Medic readById(int id) {
        final String sql = """
            SELECT m.id, pe.nome, pe.cpf, m.email, m.senha, pe.sexo, pe.data_nascimento, 
                   m.crm, m.ativo
            FROM medico m
            JOIN pessoa pe ON m.pessoa_id = pe.id
            WHERE m.id = ? AND m.ativo = true
        """;

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Medic medic = buildMedicFromResultSet(resultSet);
                preparedStatement.close();
                resultSet.close();
                return medic;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public List<Medic> readAll() {
        final String sql = """
            SELECT m.id, pe.nome, pe.cpf, m.email, m.senha, pe.sexo, pe.data_nascimento, 
                   m.crm, m.ativo
            FROM medico m
            JOIN pessoa pe ON m.pessoa_id = pe.id
            WHERE m.ativo = true
        """;

        List<Medic> medics = new ArrayList<>();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                medics.add(buildMedicFromResultSet(resultSet));
            }

            preparedStatement.close();
            resultSet.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return medics;
    }

    // Remove @Override - este método não está na interface
    public void update(Medic entity) {
        logger.log(Level.INFO, "Preparando para atualizar o médico");

        try {
            connection.setAutoCommit(false);

            // Atualizar pessoa
            String sqlPessoa = "UPDATE pessoa SET nome = ?, cpf = ?, sexo = ?, data_nascimento = ? WHERE id = (SELECT pessoa_id FROM medico WHERE id = ?)";
            PreparedStatement preparedStatementPessoa = connection.prepareStatement(sqlPessoa);
            preparedStatementPessoa.setString(1, entity.getName());
            preparedStatementPessoa.setString(2, entity.getCpf());
            preparedStatementPessoa.setString(3, entity.getGender() == Gender.MASCULINO ? "M" : "F");
            preparedStatementPessoa.setDate(4, java.sql.Date.valueOf(entity.getBirthDate()));
            preparedStatementPessoa.setInt(5, entity.getId());
            preparedStatementPessoa.execute();
            preparedStatementPessoa.close();

            // Atualizar médico
            String sql = "UPDATE medico SET email = ?, crm = ?, ativo = ? WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, entity.getEmail());
            preparedStatement.setString(2, entity.getCrm());
            preparedStatement.setBoolean(3, entity.isActive());
            preparedStatement.setInt(4, entity.getId());
            preparedStatement.execute();
            preparedStatement.close();

            connection.commit();
            logger.log(Level.INFO, "Médico atualizado com sucesso.");

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
    public void updateInformation(int id, Medic entity) {
        // Atualiza informações específicas do médico
        entity.setId(id);
        update(entity);
    }

    @Override
    public Medic tofindByEmail(String email) {
        final String sql = """
            SELECT m.id, pe.nome, pe.cpf, m.email, m.senha, pe.sexo, pe.data_nascimento, 
                   m.crm, m.ativo
            FROM medico m
            JOIN pessoa pe ON m.pessoa_id = pe.id
            WHERE m.email = ? AND m.ativo = true
        """;

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, email);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Medic medic = buildMedicFromResultSet(resultSet);
                preparedStatement.close();
                resultSet.close();
                return medic;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public Medic findByEmail(String email) {
        final String sql = "SELECT m.id, pe.nome, pe.cpf, m.email, m.senha, pe.sexo, pe.data_nascimento, m.crm, m.ativo FROM medico m JOIN pessoa pe ON m.pessoa_id = pe.id WHERE m.email = ? AND m.ativo = true";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Medic medic = buildMedicFromResultSet(resultSet);
                preparedStatement.close();
                resultSet.close();
                return medic;
            }
            preparedStatement.close();
            resultSet.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public Medic findByEmailAndPassword(String email, String password) {
        final String sql = "SELECT m.id, pe.nome, pe.cpf, m.email, m.senha, pe.sexo, pe.data_nascimento, m.crm, m.ativo FROM medico m JOIN pessoa pe ON m.pessoa_id = pe.id WHERE m.email = ? AND m.senha = crypt(?::text, m.senha::text) AND m.ativo = true";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Medic medic = buildMedicFromResultSet(resultSet);
                preparedStatement.close();
                resultSet.close();
                return medic;
            }
            preparedStatement.close();
            resultSet.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    private Medic buildMedicFromResultSet(ResultSet resultSet) throws SQLException {
        return Medic.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("nome"))
                .cpf(resultSet.getString("cpf"))
                .email(resultSet.getString("email"))
                .password(resultSet.getString("senha"))
                .gender(resultSet.getString("sexo").equals("M") ? Gender.MASCULINO : Gender.FEMININO)
                .birthDate(resultSet.getDate("data_nascimento").toLocalDate())
                .crm(resultSet.getString("crm"))
                .specialty("Clínica Geral") // Valor padrão, pode ser melhorado com join para especialidades
                .active(resultSet.getBoolean("ativo"))
                .build();
    }
}
