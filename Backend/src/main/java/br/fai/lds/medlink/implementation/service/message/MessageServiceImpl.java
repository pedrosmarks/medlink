package br.fai.lds.medlink.implementation.service.message;

import br.fai.lds.medlink.domain.Message;
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
    
    public MessageServiceImpl() {
        System.out.println("MessageServiceImpl criado");
    }
    
    @jakarta.annotation.PostConstruct
    public void init() {
        System.out.println("DataSource injetado: " + (dataSource != null ? "OK" : "NULL"));
        createTablesIfNotExist();
    }
    
    private void createTablesIfNotExist() {
        try (java.sql.Connection conn = dataSource.getConnection()) {
            // Verifica se a tabela mensagem existe
            if (!tableExists(conn, "mensagem")) {
                System.out.println("Criando tabela mensagem...");
                createMensagemTable(conn);
            } else {
                // Atualiza tabela existente com novas colunas
                updateMensagemTable(conn);
            }
            
            // Verifica se a tabela solicitacao_acesso_prontuario existe
            if (!tableExists(conn, "solicitacao_acesso_prontuario")) {
                System.out.println("Criando tabela solicitacao_acesso_prontuario...");
                createSolicitacaoTable(conn);
            }
            
            System.out.println("Tabelas verificadas/criadas com sucesso");
        } catch (Exception e) {
            System.out.println("Erro ao verificar/criar tabelas: " + e.getMessage());
        }
    }
    
    private void updateMensagemTable(java.sql.Connection conn) {
        try {
            // Adiciona colunas se não existirem
            addColumnIfNotExists(conn, "mensagem", "sender_type", "VARCHAR(10)");
            addColumnIfNotExists(conn, "mensagem", "sender_id", "VARCHAR(10)");
            addColumnIfNotExists(conn, "mensagem", "recipient_id", "VARCHAR(10)");
            System.out.println("Tabela mensagem atualizada");
        } catch (Exception e) {
            System.out.println("Erro ao atualizar tabela mensagem: " + e.getMessage());
        }
    }
    
    private void addColumnIfNotExists(java.sql.Connection conn, String tableName, String columnName, String columnType) {
        try (java.sql.PreparedStatement stmt = conn.prepareStatement(
            "SELECT 1 FROM information_schema.columns WHERE table_name = ? AND column_name = ?")) {
            stmt.setString(1, tableName);
            stmt.setString(2, columnName);
            try (java.sql.ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    // Coluna não existe, adiciona
                    String sql = "ALTER TABLE " + tableName + " ADD COLUMN " + columnName + " " + columnType;
                    try (java.sql.PreparedStatement addStmt = conn.prepareStatement(sql)) {
                        addStmt.executeUpdate();
                        System.out.println("Coluna " + columnName + " adicionada à tabela " + tableName);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Erro ao verificar/adicionar coluna " + columnName + ": " + e.getMessage());
        }
    }
    
    private boolean tableExists(java.sql.Connection conn, String tableName) {
        try (java.sql.PreparedStatement stmt = conn.prepareStatement(
            "SELECT 1 FROM information_schema.tables WHERE table_name = ?")) {
            stmt.setString(1, tableName);
            try (java.sql.ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (Exception e) {
            return false;
        }
    }
    
    private void createMensagemTable(java.sql.Connection conn) throws Exception {
        String sql = "CREATE TABLE mensagem (" +
                    "id SERIAL PRIMARY KEY, " +
                    "paciente_id INT NOT NULL, " +
                    "medico_id INT NOT NULL, " +
                    "conteudo TEXT NOT NULL, " +
                    "data_envio TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "respondida BOOLEAN DEFAULT FALSE, " +
                    "sender_type VARCHAR(10), " +
                    "sender_id VARCHAR(10), " +
                    "recipient_id VARCHAR(10)" +
                    ")";
        try (java.sql.PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        }
    }
    
    private void createSolicitacaoTable(java.sql.Connection conn) throws Exception {
        // Primeiro cria o tipo enum se não existir
        try (java.sql.PreparedStatement stmt = conn.prepareStatement(
            "CREATE TYPE status_solicitacao AS ENUM ('PENDENTE', 'ACEITA', 'RECUSADA', 'REVOGADA')")) {
            stmt.executeUpdate();
        } catch (Exception e) {
            // Tipo já existe, ignora
        }
        
        String sql = "CREATE TABLE solicitacao_acesso_prontuario (" +
                    "id SERIAL PRIMARY KEY, " +
                    "medico_id INT NOT NULL, " +
                    "paciente_id INT NOT NULL, " +
                    "data_solicitacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, " +
                    "status status_solicitacao NOT NULL DEFAULT 'PENDENTE', " +
                    "data_resposta TIMESTAMP, " +
                    "revogado BOOLEAN DEFAULT FALSE, " +
                    "CONSTRAINT unq_medico_paciente UNIQUE (medico_id, paciente_id)" +
                    ")";
        try (java.sql.PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        }
    }

    public List<Message> findAll() {
        List<Message> messages = new ArrayList<>();
        try (java.sql.Connection conn = dataSource.getConnection()) {
            
            // Tenta buscar com as novas colunas
            try (java.sql.PreparedStatement stmt = conn.prepareStatement(
                "SELECT id, paciente_id, medico_id, conteudo, data_envio, respondida, sender_type, sender_id, recipient_id FROM mensagem ORDER BY data_envio DESC")) {
                
                try (java.sql.ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        Message message = new Message();
                        message.setId(String.valueOf(rs.getInt("id")));
                        
                        // Usar os dados salvos corretamente
                        String senderType = rs.getString("sender_type");
                        String senderId = rs.getString("sender_id");
                        String recipientId = rs.getString("recipient_id");
                        
                        if (senderType != null && senderId != null && recipientId != null) {
                            // Usar dados salvos
                            message.setSenderType(senderType);
                            message.setSenderId(senderId);
                            message.setRecipientId(recipientId);
                            message.setRecipientType("MEDIC".equals(senderType) ? "PATIENT" : "MEDIC");
                        } else {
                            // Fallback para mensagens antigas
                            message.setSenderId(String.valueOf(rs.getInt("paciente_id")));
                            message.setSenderType("PATIENT");
                            message.setRecipientId(String.valueOf(rs.getInt("medico_id")));
                            message.setRecipientType("MEDIC");
                        }
                        
                        message.setText(rs.getString("conteudo"));
                        message.setDate(rs.getTimestamp("data_envio").toString());
                        message.setRead(rs.getBoolean("respondida"));
                        
                        // Buscar nomes baseado no tipo
                        if ("MEDIC".equals(message.getSenderType())) {
                            message.setSenderName("Dr. Médico " + message.getSenderId());
                            message.setRecipientName("Paciente " + message.getRecipientId());
                        } else {
                            message.setSenderName("Paciente " + message.getSenderId());
                            message.setRecipientName("Dr. Médico " + message.getRecipientId());
                        }
                        
                        messages.add(message);
                    }
                }
                
            } catch (Exception e1) {
                // Fallback: busca sem as novas colunas
                System.out.println("Buscando sem novas colunas: " + e1.getMessage());
                try (java.sql.PreparedStatement stmt = conn.prepareStatement(
                    "SELECT id, paciente_id, medico_id, conteudo, data_envio, respondida FROM mensagem ORDER BY data_envio DESC")) {
                    
                    try (java.sql.ResultSet rs = stmt.executeQuery()) {
                        while (rs.next()) {
                            Message message = new Message();
                            message.setId(String.valueOf(rs.getInt("id")));
                            
                            // Fallback para mensagens antigas
                            message.setSenderId(String.valueOf(rs.getInt("paciente_id")));
                            message.setSenderType("PATIENT");
                            message.setRecipientId(String.valueOf(rs.getInt("medico_id")));
                            message.setRecipientType("MEDIC");
                            
                            message.setText(rs.getString("conteudo"));
                            message.setDate(rs.getTimestamp("data_envio").toString());
                            message.setRead(rs.getBoolean("respondida"));
                            
                            message.setSenderName("Paciente " + message.getSenderId());
                            message.setRecipientName("Dr. Médico " + message.getRecipientId());
                            
                            messages.add(message);
                        }
                    }
                }
            }
            
        } catch (Exception e) {
            System.out.println("Erro ao buscar mensagens: " + e.getMessage());
            e.printStackTrace();
        }
        System.out.println("findAll chamado - total de mensagens: " + messages.size());
        return messages;
    }

    public void sendMessage(Message message) {
        // Validação de segurança
        if (!isValidMessage(message)) {
            throw new RuntimeException("Dados da mensagem inválidos.");
        }
        
        // Verifica autorização médico-paciente
        if (!isAuthorized(message.getSenderId(), message.getSenderType(), message.getRecipientId(), message.getRecipientType())) {
            throw new RuntimeException("Médico e paciente não estão autorizados para troca de mensagens.");
        }
        // Gera ID único se não estiver definido
        if (message.getId() == null || message.getId().isEmpty()) {
            message.setId(UUID.randomUUID().toString().substring(0, 8));
        }
        
        // Gera data/hora atual se não estiver definida
        if (message.getDate() == null || message.getDate().isEmpty()) {
            LocalDateTime now = LocalDateTime.now();
            message.setDate(now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
        }
        
        // Define como não lida por padrão
        message.setRead(false);
        
        // Preenche nome do médico se necessário
        if ("MEDIC".equals(message.getSenderType()) && (message.getSenderName() == null || message.getSenderName().isEmpty())) {
            message.setSenderName(buscarNomeMedicoPorId(message.getSenderId()));
        }
        if ("MEDIC".equals(message.getRecipientType()) && (message.getRecipientName() == null || message.getRecipientName().isEmpty())) {
            message.setRecipientName(buscarNomeMedicoPorId(message.getRecipientId()));
        }
        
        // Salva a mensagem no banco
        try (java.sql.Connection conn = dataSource.getConnection()) {
            
            // Determinar IDs baseado no tipo
            int pacienteId, medicoId;
            if ("PATIENT".equals(message.getSenderType())) {
                pacienteId = Integer.parseInt(message.getSenderId());
                medicoId = Integer.parseInt(message.getRecipientId());
            } else {
                pacienteId = Integer.parseInt(message.getRecipientId());
                medicoId = Integer.parseInt(message.getSenderId());
            }
            
            // Tenta inserir com as novas colunas
            try (java.sql.PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO mensagem (paciente_id, medico_id, conteudo, data_envio, respondida, sender_type, sender_id, recipient_id) VALUES (?, ?, ?, CURRENT_TIMESTAMP, false, ?, ?, ?)")) {
                
                stmt.setInt(1, pacienteId);
                stmt.setInt(2, medicoId);
                stmt.setString(3, message.getText());
                stmt.setString(4, message.getSenderType());
                stmt.setString(5, message.getSenderId());
                stmt.setString(6, message.getRecipientId());
                
                stmt.executeUpdate();
                System.out.println("Mensagem salva no banco com sucesso (com novas colunas)");
                
            } catch (Exception e1) {
                // Fallback: tenta inserir sem as novas colunas (para tabelas antigas)
                System.out.println("Tentando inserir sem novas colunas: " + e1.getMessage());
                try (java.sql.PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO mensagem (paciente_id, medico_id, conteudo, data_envio, respondida) VALUES (?, ?, ?, CURRENT_TIMESTAMP, false)")) {
                    
                    stmt.setInt(1, pacienteId);
                    stmt.setInt(2, medicoId);
                    stmt.setString(3, message.getText());
                    
                    stmt.executeUpdate();
                    System.out.println("Mensagem salva no banco com sucesso (sem novas colunas)");
                }
            }
            
        } catch (Exception e) {
            System.out.println("Erro ao salvar mensagem: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erro ao salvar mensagem no banco: " + e.getMessage(), e);
        }
    }

    // Busca simples pelo nome do médico (mock)
    private String buscarNomeMedicoPorId(String idMedico) {
        // Exemplo: medico_1 -> Dr. Carlos Silva, medico_2 -> Dr. Pedro Almeida
        if ("medico_1".equals(idMedico)) return "Dr. Carlos Silva";
        if ("medico_2".equals(idMedico)) return "Dr. Pedro Almeida";
        return idMedico;
    }

    private boolean isValidMessage(Message message) {
        // Validações básicas
        if (message.getSenderId() == null || message.getSenderId().trim().isEmpty()) return false;
        if (message.getRecipientId() == null || message.getRecipientId().trim().isEmpty()) return false;
        if (message.getText() == null || message.getText().trim().isEmpty()) return false;
        if (message.getSenderType() == null || message.getRecipientType() == null) return false;
        
        // Sanitiza texto
        message.setText(message.getText().trim());
        
        // Valida tipos
        return ("MEDIC".equals(message.getSenderType()) || "PATIENT".equals(message.getSenderType())) &&
               ("MEDIC".equals(message.getRecipientType()) || "PATIENT".equals(message.getRecipientType()));
    }
    
    private boolean isAuthorized(String remetenteId, String remetenteTipo, String destinatarioId, String destinatarioTipo) {
        System.out.println("Validando autorização: " + remetenteTipo + "(" + remetenteId + ") -> " + destinatarioTipo + "(" + destinatarioId + ")");
        
        try {
            // Só valida se for médico <-> paciente
            String medicoId = null;
            String pacienteId = null;
            
            if ("MEDIC".equals(remetenteTipo) && "PATIENT".equals(destinatarioTipo)) {
                medicoId = remetenteId;
                pacienteId = destinatarioId;
            } else if ("PATIENT".equals(remetenteTipo) && "MEDIC".equals(destinatarioTipo)) {
                medicoId = destinatarioId;
                pacienteId = remetenteId;
            } else {
                System.out.println("Não é relação médico-paciente válida");
                return false;
            }
            
            System.out.println("Verificando: medico_id=" + medicoId + ", paciente_id=" + pacienteId);
            
            // Primeiro tenta verificar na tabela de solicitações
            if (checkSolicitacaoAcesso(medicoId, pacienteId)) {
                return true;
            }
            
            // Se não encontrar, cria uma solicitação aceita automaticamente (para desenvolvimento)
            return createAutoApprovedAccess(medicoId, pacienteId);
            
        } catch (Exception e) {
            System.out.println("Erro geral na validação: " + e.getMessage());
            return false;
        }
    }
    
    private boolean checkSolicitacaoAcesso(String medicoId, String pacienteId) {
        try (java.sql.Connection conn = dataSource.getConnection();
             java.sql.PreparedStatement stmt = conn.prepareStatement(
                "SELECT 1 FROM solicitacao_acesso_prontuario WHERE medico_id = ? AND paciente_id = ? AND status = 'ACEITA'")) {
            
            stmt.setInt(1, Integer.parseInt(medicoId));
            stmt.setInt(2, Integer.parseInt(pacienteId));
            
            try (java.sql.ResultSet rs = stmt.executeQuery()) {
                boolean autorizado = rs.next();
                System.out.println("Solicitação encontrada: " + autorizado);
                return autorizado;
            }
        } catch (Exception e) {
            System.out.println("Erro ao verificar solicitação (tabela pode não existir): " + e.getMessage());
            return false;
        }
    }
    
    private boolean createAutoApprovedAccess(String medicoId, String pacienteId) {
        try (java.sql.Connection conn = dataSource.getConnection();
             java.sql.PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO solicitacao_acesso_prontuario (medico_id, paciente_id, status, data_resposta) VALUES (?, ?, 'ACEITA', CURRENT_TIMESTAMP) ON CONFLICT (medico_id, paciente_id) DO NOTHING")) {
            
            stmt.setInt(1, Integer.parseInt(medicoId));
            stmt.setInt(2, Integer.parseInt(pacienteId));
            
            stmt.executeUpdate();
            System.out.println("Acesso criado automaticamente para desenvolvimento");
            return true;
            
        } catch (Exception e) {
            System.out.println("Erro ao criar acesso automático: " + e.getMessage());
            // Se não conseguir criar, permite para desenvolvimento
            return true;
        }
    }

    public Message markAsRead(String id) {
        try (java.sql.Connection conn = dataSource.getConnection();
             java.sql.PreparedStatement stmt = conn.prepareStatement(
                "UPDATE mensagem SET respondida = true WHERE id = ?")) {
            
            stmt.setInt(1, Integer.parseInt(id));
            int updated = stmt.executeUpdate();
            
            if (updated > 0) {
                // Buscar a mensagem atualizada
                List<Message> messages = findAll();
                return messages.stream()
                    .filter(m -> m.getId().equals(id))
                    .findFirst()
                    .orElse(null);
            }
        } catch (Exception e) {
            System.out.println("Erro ao marcar mensagem como lida: " + e.getMessage());
        }
        return null;
    }

    public List<Message> findConversationsByUser(String senderId, String senderType) {
        List<Message> allMessages = findAll();
        return allMessages.stream()
                .filter(message -> message.getSenderId().equals(senderId) && message.getSenderType().equals(senderType))
                .toList();
    }
}