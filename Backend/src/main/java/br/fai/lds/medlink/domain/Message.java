package br.fai.lds.medlink.domain;

public class Message {
    private String id;
    private String senderId;
    private String senderType;
    private String senderName;
    private String recipientId;
    private String recipientType;
    private String recipientName;
    private String text;
    private String date;
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