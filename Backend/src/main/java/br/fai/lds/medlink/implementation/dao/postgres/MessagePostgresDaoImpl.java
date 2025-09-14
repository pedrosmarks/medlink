package br.fai.lds.medlink.implementation.dao.postgres;

import br.fai.lds.medlink.domain.Message;
import br.fai.lds.medlink.port.dao.message.MessageDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
    public void create(Message entity) {
        logger.log(Level.INFO, "Preparando para adicionar mensagem no banco de dados");
        String sql = "INSERT INTO mensagem(sender_id, sender_type, sender_name, recipient_id, recipient_type, recipient_name, text, date, read) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, entity.getSenderId());
            preparedStatement.setString(2, entity.getSenderType());
            preparedStatement.setString(3, entity.getSenderName());
            preparedStatement.setString(4, entity.getRecipientId());
            preparedStatement.setString(5, entity.getRecipientType());
            preparedStatement.setString(6, entity.getRecipientName());
            preparedStatement.setString(7, entity.getText());
            preparedStatement.setString(8, entity.getDate());
            preparedStatement.setBoolean(9, entity.isRead());

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
    public Message readById(int id) {
        String sql = "SELECT * FROM mensagem WHERE id = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Message mensagem = buildMessageFromResultSet(resultSet);
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
    public List<Message> readAll() {
        String sql = "SELECT * FROM mensagem ORDER BY date DESC";
        List<Message> mensagens = new ArrayList<>();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                mensagens.add(buildMessageFromResultSet(resultSet));
            }

            preparedStatement.close();
            resultSet.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return mensagens;
    }

    @Override
    public void updateInformation(int id, Message entity) {
        String sql = "UPDATE mensagem SET text = ?, read = ? WHERE id = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, entity.getText());
            preparedStatement.setBoolean(2, entity.isRead());
            preparedStatement.setInt(3, id);

            preparedStatement.execute();
            preparedStatement.close();
            logger.log(Level.INFO, "Mensagem atualizada com sucesso.");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Message> findByUserId(String userId, String userType) {
        String sql = "SELECT * FROM mensagem WHERE (sender_id = ? AND sender_type = ?) OR (recipient_id = ? AND recipient_type = ?) ORDER BY date DESC";
        List<Message> mensagens = new ArrayList<>();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, userId);
            preparedStatement.setString(2, userType);
            preparedStatement.setString(3, userId);
            preparedStatement.setString(4, userType);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                mensagens.add(buildMessageFromResultSet(resultSet));
            }

            preparedStatement.close();
            resultSet.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return mensagens;
    }

    @Override
    public List<Message> findConversationBetweenUsers(String user1Id, String user1Type, String user2Id, String user2Type) {
        String sql = """
            SELECT * FROM mensagem 
            WHERE ((sender_id = ? AND sender_type = ?) AND (recipient_id = ? AND recipient_type = ?))
               OR ((sender_id = ? AND sender_type = ?) AND (recipient_id = ? AND recipient_type = ?))
            ORDER BY date ASC
        """;
        List<Message> mensagens = new ArrayList<>();

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
                mensagens.add(buildMessageFromResultSet(resultSet));
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
        String sql = "UPDATE mensagem SET read = true WHERE id = ?";

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
    public List<Message> findUnreadMessages(String userId, String userType) {
        String sql = "SELECT * FROM mensagem WHERE recipient_id = ? AND recipient_type = ? AND read = false ORDER BY date DESC";
        List<Message> mensagens = new ArrayList<>();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, userId);
            preparedStatement.setString(2, userType);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                mensagens.add(buildMessageFromResultSet(resultSet));
            }

            preparedStatement.close();
            resultSet.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return mensagens;
    }

    private Message buildMessageFromResultSet(ResultSet resultSet) throws SQLException {
        Message message = new Message();
        message.setId(String.valueOf(resultSet.getInt("id")));
        message.setSenderId(resultSet.getString("sender_id"));
        message.setSenderType(resultSet.getString("sender_type"));
        message.setSenderName(resultSet.getString("sender_name"));
        message.setRecipientId(resultSet.getString("recipient_id"));
        message.setRecipientType(resultSet.getString("recipient_type"));
        message.setRecipientName(resultSet.getString("recipient_name"));
        message.setText(resultSet.getString("text"));
        message.setDate(resultSet.getString("date"));
        message.setRead(resultSet.getBoolean("read"));
        return message;
    }
}
