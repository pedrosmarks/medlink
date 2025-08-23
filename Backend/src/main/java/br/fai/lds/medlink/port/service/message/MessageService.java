package br.fai.lds.medlink.port.service.message;

import br.fai.lds.medlink.domain.Mensagem;
import java.util.List;

public interface MessageService {
    
    List<Mensagem> findAll();
    
    void sendMessage(Mensagem message);
    
    Mensagem markAsRead(String id);
    
    List<Mensagem> findConversationsByUser(String senderId, String senderType);
}