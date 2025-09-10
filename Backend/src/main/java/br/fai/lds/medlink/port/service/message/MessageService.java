package br.fai.lds.medlink.port.service.message;

import br.fai.lds.medlink.domain.Message;
import java.util.List;

public interface MessageService {
    
    List<Message> findAll();
    
    void sendMessage(Message message);
    
    Message markAsRead(String id);
    
    List<Message> findConversationsByUser(String senderId, String senderType);
}