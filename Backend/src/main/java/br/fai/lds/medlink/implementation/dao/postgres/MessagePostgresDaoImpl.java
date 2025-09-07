package br.fai.lds.medlink.implementation.dao.postgres;

import br.fai.lds.medlink.domain.Mensagem;
import br.fai.lds.medlink.port.dao.message.MessageDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MessagePostgresDaoImpl implements MessageDao {

    private static final Logger logger = Logger.getLogger(MessagePostgresDaoImpl.class.getName());
    private final Connection connection;

    public MessagePostgresDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void create(Mensagem entity) {
        logger.log(Level.INFO, "Preparando para adicionar mensagem no banco de dados");
    String sql = "INSERT INTO mensagem(remetente_id, remetente_tipo, remetente_nome, destinatario_id, destinatario_tipo, destinatario_nome, texto, data, lida) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, entity.getRemetenteId());
            preparedStatement.setString(2, entity.getRemetenteTipo());
            preparedStatement.setString(3, entity.getRemetenteNome());
            preparedStatement.setString(4, entity.getDestinatarioId());
            preparedStatement.setString(5, entity.getDestinatarioTipo());
            preparedStatement.setString(6, entity.getDestinatarioNome());
            preparedStatement.setString(7, entity.getTexto());
            preparedStatement.setString(8, entity.getData());
            preparedStatement.setBoolean(9, entity.isLida());

            preparedStatement.execute();
            preparedStatement.close();
            logger.log(Level.INFO, "Mensagem adicionada com sucesso.");

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Problema ao adicionar mensagem no banco de dados.", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean remove(int id) {
        logger.log(Level.INFO, "Preparando para remover mensagem");
        String sql = "DELETE FROM mensagem WHERE id = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            int rowsAffected = preparedStatement.executeUpdate();
            preparedStatement.close();

            boolean success = rowsAffected > 0;
            if (success) {
                logger.log(Level.INFO, "Mensagem removida com sucesso.");
            } else {
                logger.log(Level.WARNING, "Nenhuma mensagem foi removida - ID não encontrado.");
            }
            return success;

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erro ao remover mensagem.", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Mensagem readById(int id) {
        String sql = "SELECT * FROM mensagem WHERE id = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Mensagem mensagem = buildMensagemFromResultSet(resultSet);
                preparedStatement.close();
                resultSet.close();
                return mensagem;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public List<Mensagem> readAll() {
        String sql = "SELECT * FROM mensagem ORDER BY data DESC";
        List<Mensagem> mensagens = new ArrayList<>();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                mensagens.add(buildMensagemFromResultSet(resultSet));
            }

            preparedStatement.close();
            resultSet.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return mensagens;
    }

    @Override
    public void updateInformation(int id, Mensagem entity) {
    String sql = "UPDATE mensagem SET texto = ?, lida = ? WHERE id = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, entity.getTexto());
            preparedStatement.setBoolean(2, entity.isLida());
            preparedStatement.setInt(3, id);

            preparedStatement.execute();
            preparedStatement.close();
            logger.log(Level.INFO, "Mensagem atualizada com sucesso.");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Mensagem> findByUserId(String userId, String userType) {
        String sql = "SELECT * FROM mensagem WHERE (remetente_id = ? AND remetente_tipo = ?) OR (destinatario_id = ? AND destinatario_tipo = ?) ORDER BY data DESC";
        List<Mensagem> mensagens = new ArrayList<>();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, userId);
            preparedStatement.setString(2, userType);
            preparedStatement.setString(3, userId);
            preparedStatement.setString(4, userType);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                mensagens.add(buildMensagemFromResultSet(resultSet));
            }

            preparedStatement.close();
            resultSet.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return mensagens;
    }

    @Override
    public List<Mensagem> findConversationBetweenUsers(String user1Id, String user1Type, String user2Id, String user2Type) {
        String sql = """
            SELECT * FROM mensagem 
            WHERE ((remetente_id = ? AND remetente_tipo = ?) AND (destinatario_id = ? AND destinatario_tipo = ?))
               OR ((remetente_id = ? AND remetente_tipo = ?) AND (destinatario_id = ? AND destinatario_tipo = ?))
            ORDER BY data ASC
        """;
        List<Mensagem> mensagens = new ArrayList<>();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, user1Id);
            preparedStatement.setString(2, user1Type);
            preparedStatement.setString(3, user2Id);
            preparedStatement.setString(4, user2Type);
            preparedStatement.setString(5, user2Id);
            preparedStatement.setString(6, user2Type);
            preparedStatement.setString(7, user1Id);
            preparedStatement.setString(8, user1Type);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                mensagens.add(buildMensagemFromResultSet(resultSet));
            }

            preparedStatement.close();
            resultSet.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return mensagens;
    }

    @Override
    public void markAsRead(String messageId) {
        String sql = "UPDATE mensagem SET lida = true WHERE id = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, Integer.parseInt(messageId));
            preparedStatement.execute();
            preparedStatement.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Mensagem> findUnreadMessages(String userId, String userType) {
        String sql = "SELECT * FROM mensagem WHERE destinatario_id = ? AND destinatario_tipo = ? AND lida = false ORDER BY data DESC";
        List<Mensagem> mensagens = new ArrayList<>();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, userId);
            preparedStatement.setString(2, userType);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                mensagens.add(buildMensagemFromResultSet(resultSet));
            }

            preparedStatement.close();
            resultSet.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return mensagens;
    }

    private Mensagem buildMensagemFromResultSet(ResultSet resultSet) throws SQLException {
        Mensagem mensagem = new Mensagem();
        mensagem.setId(String.valueOf(resultSet.getInt("id")));
        mensagem.setRemetenteId(resultSet.getString("remetente_id"));
        mensagem.setRemetenteTipo(resultSet.getString("remetente_tipo"));
        mensagem.setRemetenteNome(resultSet.getString("remetente_nome"));
        mensagem.setDestinatarioId(resultSet.getString("destinatario_id"));
        mensagem.setDestinatarioTipo(resultSet.getString("destinatario_tipo"));
        mensagem.setDestinatarioNome(resultSet.getString("destinatario_nome"));
    mensagem.setTexto(resultSet.getString("texto"));
        mensagem.setData(resultSet.getString("data"));
        mensagem.setLida(resultSet.getBoolean("lida"));
        return mensagem;
    }
}
