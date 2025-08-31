package br.fai.lds.medlink.implementation.service.message;

import br.fai.lds.medlink.domain.Mensagem;
import br.fai.lds.medlink.implementation.dao.MensagesFakeDao;
import br.fai.lds.medlink.port.service.message.MessageService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
public class MessageServiceImpl implements MessageService {

    private final MensagesFakeDao fakeDao = new MensagesFakeDao();

    public List<Mensagem> findAll() {
        return fakeDao.listarTodas();
    }

    public void sendMessage(Mensagem message) {
        // Gera ID único se não estiver definido
        if (message.getId() == null || message.getId().isEmpty()) {
            message.setId(UUID.randomUUID().toString().substring(0, 8));
        }
        
        // Gera data/hora atual se não estiver definida
        if (message.getData() == null || message.getData().isEmpty()) {
            LocalDateTime now = LocalDateTime.now();
            message.setData(now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
        }
        
        // Define como não lida por padrão
        message.setLida(false);
        
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