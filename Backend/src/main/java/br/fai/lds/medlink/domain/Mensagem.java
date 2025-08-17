package br.fai.lds.medlink.domain;

public class Mensagem {
    private String id;
    private String remetenteId;
    private String remetenteTipo;
    private String remetenteNome;
    private String destinatarioId;
    private String destinatarioTipo;
    private String destinatarioNome;
    private String texto;
    private String data;
    private boolean lida;

    public Mensagem() {
        // Construtor padrão necessário para serialização/deserialização
    }

    public Mensagem(String id, String remetenteId, String remetenteTipo, String remetenteNome,
                    String destinatarioId, String destinatarioTipo, String destinatarioNome,
                    String texto, String data, boolean lida) {
        this.id = id;
        this.remetenteId = remetenteId;
        this.remetenteTipo = remetenteTipo;
        this.remetenteNome = remetenteNome;
        this.destinatarioId = destinatarioId;
        this.destinatarioTipo = destinatarioTipo;
        this.destinatarioNome = destinatarioNome;
        this.texto = texto;
        this.data = data;
        this.lida = lida;
    }

    // Getters e setters para todos os campos
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getRemetenteId() { return remetenteId; }
    public void setRemetenteId(String remetenteId) { this.remetenteId = remetenteId; }
    public String getRemetenteTipo() { return remetenteTipo; }
    public void setRemetenteTipo(String remetenteTipo) { this.remetenteTipo = remetenteTipo; }
    public String getRemetenteNome() { return remetenteNome; }
    public void setRemetenteNome(String remetenteNome) { this.remetenteNome = remetenteNome; }
    public String getDestinatarioId() { return destinatarioId; }
    public void setDestinatarioId(String destinatarioId) { this.destinatarioId = destinatarioId; }
    public String getDestinatarioTipo() { return destinatarioTipo; }
    public void setDestinatarioTipo(String destinatarioTipo) { this.destinatarioTipo = destinatarioTipo; }
    public String getDestinatarioNome() { return destinatarioNome; }
    public void setDestinatarioNome(String destinatarioNome) { this.destinatarioNome = destinatarioNome; }
    public String getTexto() { return texto; }
    public void setTexto(String texto) { this.texto = texto; }
    public String getData() { return data; }
    public void setData(String data) { this.data = data; }
    public boolean isLida() { return lida; }
    public void setLida(boolean lida) { this.lida = lida; }
}