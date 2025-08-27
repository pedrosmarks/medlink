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
        // Preenche nome do médico se necessário
        if ("medico".equals(message.getRemetenteTipo()) && (message.getRemetenteNome() == null || message.getRemetenteNome().isEmpty())) {
            message.setRemetenteNome(buscarNomeMedicoPorId(message.getRemetenteId()));
        }
        if ("medico".equals(message.getDestinatarioTipo()) && (message.getDestinatarioNome() == null || message.getDestinatarioNome().isEmpty())) {
            message.setDestinatarioNome(buscarNomeMedicoPorId(message.getDestinatarioId()));
        }
        fakeDao.adicionarMensagem(message);

    }

    // Busca simples pelo nome do médico (mock)
    private String buscarNomeMedicoPorId(String idMedico) {
        // Exemplo: medico_1 -> Dr. Carlos Silva, medico_2 -> Dr. Pedro Almeida
        if ("medico_1".equals(idMedico)) return "Dr. Carlos Silva";
        if ("medico_2".equals(idMedico)) return "Dr. Pedro Almeida";
        return idMedico;
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