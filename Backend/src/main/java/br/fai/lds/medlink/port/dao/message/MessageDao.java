package br.fai.lds.medlink.port.dao.message;

import br.fai.lds.medlink.domain.Message;
import br.fai.lds.medlink.port.dao.crud.CrudDao;

import java.util.List;

public interface MessageDao extends CrudDao<Message> {

    List<Message> findByUserId(String userId, String userType);

    List<Message> findConversationBetweenUsers(String user1Id, String user1Type, String user2Id, String user2Type);

    void markAsRead(String messageId);

    List<Message> findUnreadMessages(String userId, String userType);
}
