package br.fai.lds.medlink.implementation.service.message;

import br.fai.lds.medlink.domain.Mensagem;
import br.fai.lds.medlink.implementation.dao.MensagesFakeDao;
import br.fai.lds.medlink.port.service.message.MessageService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    private final MensagesFakeDao fakeDao = new MensagesFakeDao();

    public List<Mensagem> findAll() {
        return fakeDao.listarTodas();
    }

    public void sendMessage(Mensagem message) {
        fakeDao.adicionarMensagem(message);
    }

    public Mensagem markAsRead(String id) {
        for (Mensagem message : fakeDao.listarTodas()) {
            if (message.getId().equals(id)) {
                message.setLida(true);
                return message;
            }
        }
        return null;
    }

    public List<Mensagem> findConversationsByUser(String senderId, String senderType) {
        return fakeDao.listarTodas().stream()
                .filter(message -> message.getRemetenteId().equals(senderId) && message.getRemetenteTipo().equals(senderType))
                .toList();
    }
}