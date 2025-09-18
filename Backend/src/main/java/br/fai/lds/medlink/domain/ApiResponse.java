package br.fai.lds.medlink.domain;

/**
 * Classe genérica para padronizar respostas da API.
 * 
 * <p>Utilizada para retornar mensagens e dados de forma consistente em todos
 * os endpoints da API, facilitando o tratamento no frontend.</p>
 * 
 * @param <T> Tipo dos dados retornados na resposta
 * @author MedLink Team
 * @version 1.0
 * @since 1.0
 */
public class ApiResponse<T> {

    /** Mensagem descritiva da resposta. */
    private String message;
    
    /** Dados retornados na resposta (pode ser null). */
    private T data;

    /**
     * Construtor para resposta apenas com mensagem.
     * 
     * @param message mensagem da resposta
     */
    public ApiResponse(String message) {
        this.message = message;
    }

    /**
     * Construtor para resposta com mensagem e dados.
     * 
     * @param message mensagem da resposta
     * @param data dados a serem retornados
     */
    public ApiResponse(String message, T data) {
        this.message = message;
        this.data = data;
    }

    // Getters e Setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
