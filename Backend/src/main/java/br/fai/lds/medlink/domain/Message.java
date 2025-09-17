package br.fai.lds.medlink.domain;

/**
 * Entidade que representa uma mensagem no sistema de comunicação.
 * 
 * <p>Permite a troca de mensagens entre médicos e pacientes,
 * mantendo o histórico de comunicação e status de leitura.</p>
 * 
 * @author MedLink Team
 * @version 1.0
 * @since 1.0
 */
public class Message {
    /** ID único da mensagem. */
    private String id;
    
    /** ID do remetente da mensagem. */
    private String senderId;
    
    /** Tipo do remetente (MEDICO ou PACIENTE). */
    private String senderType;
    
    /** Nome do remetente da mensagem. */
    private String senderName;
    
    /** ID do destinatário da mensagem. */
    private String recipientId;
    
    /** Tipo do destinatário (MEDICO ou PACIENTE). */
    private String recipientType;
    
    /** Nome do destinatário da mensagem. */
    private String recipientName;
    
    /** Conteúdo textual da mensagem. */
    private String text;
    
    /** Data e hora de envio da mensagem. */
    private String date;
    
    /** Indica se a mensagem foi lida pelo destinatário. */
    private boolean read;

    public Message() {
        // Default constructor for serialization/deserialization
    }

    public Message(String id, String senderId, String senderType, String senderName,
                   String recipientId, String recipientType, String recipientName,
                   String text, String date, boolean read) {
        this.id = id;
        this.senderId = senderId;
        this.senderType = senderType;
        this.senderName = senderName;
        this.recipientId = recipientId;
        this.recipientType = recipientType;
        this.recipientName = recipientName;
        this.text = text;
        this.date = date;
        this.read = read;
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getSenderId() { return senderId; }
    public void setSenderId(String senderId) { this.senderId = senderId; }
    public String getSenderType() { return senderType; }
    public void setSenderType(String senderType) { this.senderType = senderType; }
    public String getSenderName() { return senderName; }
    public void setSenderName(String senderName) { this.senderName = senderName; }
    public String getRecipientId() { return recipientId; }
    public void setRecipientId(String recipientId) { this.recipientId = recipientId; }
    public String getRecipientType() { return recipientType; }
    public void setRecipientType(String recipientType) { this.recipientType = recipientType; }
    public String getRecipientName() { return recipientName; }
    public void setRecipientName(String recipientName) { this.recipientName = recipientName; }
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public boolean isRead() { return read; }
    public void setRead(boolean read) { this.read = read; }
}