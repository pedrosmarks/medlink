package br.fai.lds.medlink.domain;

// Essa classe representa uma resposta genérica da API,
// que pode ser usada para retornar mensagens e dados de forma consistente.
public class ApiResponse<T> {

    private String message;
    private T data;

    public ApiResponse(String message) {
        this.message = message;
    }

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
