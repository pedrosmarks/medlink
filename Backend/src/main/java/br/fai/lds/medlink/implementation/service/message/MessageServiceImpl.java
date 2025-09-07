package br.fai.lds.medlink.implementation.service.message;

import br.fai.lds.medlink.domain.Mensagem;
import br.fai.lds.medlink.port.service.message.MessageService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class MessageServiceImpl implements MessageService {
    @org.springframework.beans.factory.annotation.Autowired
    private javax.sql.DataSource dataSource;

    // Lista temporária em memória até implementar o DAO do banco
    private final List<Mensagem> messages = new ArrayList<>();

    public List<Mensagem> findAll() {
        return new ArrayList<>(messages);
    }

    public void sendMessage(Mensagem message) {
        // Validação: só permite se médico e paciente estão autorizados
        if (!isAuthorized(message.getRemetenteId(), message.getRemetenteTipo(), message.getDestinatarioId(), message.getDestinatarioTipo())) {
            throw new RuntimeException("Médico e paciente não estão autorizados para troca de mensagens.");
        }
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
        
        // Adiciona a mensagem à lista
        messages.add(message);
    }

    // Busca simples pelo nome do médico (mock)
    private String buscarNomeMedicoPorId(String idMedico) {
        // Exemplo: medico_1 -> Dr. Carlos Silva, medico_2 -> Dr. Pedro Almeida
        if ("medico_1".equals(idMedico)) return "Dr. Carlos Silva";
        if ("medico_2".equals(idMedico)) return "Dr. Pedro Almeida";
        return idMedico;
    }

    private boolean isAuthorized(String remetenteId, String remetenteTipo, String destinatarioId, String destinatarioTipo) {
        try {
            // Só valida se for médico <-> paciente
            String medicoId = null;
            String pacienteId = null;
            if ("medico".equals(remetenteTipo) && "paciente".equals(destinatarioTipo)) {
                medicoId = remetenteId;
                pacienteId = destinatarioId;
            } else if ("paciente".equals(remetenteTipo) && "medico".equals(destinatarioTipo)) {
                medicoId = destinatarioId;
                pacienteId = remetenteId;
            } else {
                // Não é relação médico-paciente
                return false;
            }
            // Consulta no banco
            try (java.sql.Connection conn = dataSource.getConnection();
                 java.sql.PreparedStatement stmt = conn.prepareStatement(
                    "SELECT 1 FROM solicitacao_acesso_prontuario WHERE medico_id = ? AND paciente_id = ? AND status = 'ACEITA'::status_solicitacao")) {
                stmt.setInt(1, Integer.parseInt(medicoId));
                stmt.setInt(2, Integer.parseInt(pacienteId));
                try (java.sql.ResultSet rs = stmt.executeQuery()) {
                    return rs.next();
                }
            } catch (Exception e) {
                throw new RuntimeException("Erro ao validar autorização médico-paciente: " + e.getMessage(), e);
            }
        } catch (Exception e) {
            return false;
        }
    }

    public Mensagem markAsRead(String id) {
        for (Mensagem message : messages) {
            if (message.getId().equals(id)) {
                message.setLida(true);
                return message;
            }
        }
        return null;
    }

    public List<Mensagem> findConversationsByUser(String senderId, String senderType) {
        return messages.stream()
                .filter(message -> message.getRemetenteId().equals(senderId) && message.getRemetenteTipo().equals(senderType))
                .toList();
    }
}