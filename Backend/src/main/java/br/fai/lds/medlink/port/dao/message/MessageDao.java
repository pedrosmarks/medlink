package br.fai.lds.medlink.port.dao.message;

import br.fai.lds.medlink.domain.Mensagem;
import br.fai.lds.medlink.port.dao.crud.CrudDao;

import java.util.List;

public interface MessageDao extends CrudDao<Mensagem> {

    List<Mensagem> findByUserId(String userId, String userType);

    List<Mensagem> findConversationBetweenUsers(String user1Id, String user1Type, String user2Id, String user2Type);

    void markAsRead(String messageId);

    List<Mensagem> findUnreadMessages(String userId, String userType);
}
