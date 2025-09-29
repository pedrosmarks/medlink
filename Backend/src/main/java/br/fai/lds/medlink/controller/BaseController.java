package br.fai.lds.medlink.controller;

import br.fai.lds.medlink.domain.ApiResponse;
import br.fai.lds.medlink.util.LogSanitizer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Classe base para controllers com métodos comuns
 * Reduz duplicação de código e padroniza respostas
 */
@Slf4j
public abstract class BaseController {

    /**
     * Valida se ID é válido (maior que zero)
     */
    protected boolean isValidId(int id) {
        return id > 0;
    }

    /**
     * Cria resposta de erro para ID inválido
     */
    protected <T> ResponseEntity<ApiResponse<T>> badRequestInvalidId() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse<>("ID deve ser maior que zero."));
    }

    /**
     * Cria resposta de erro para entidade não encontrada
     */
    protected <T> ResponseEntity<ApiResponse<T>> notFound(String entityName) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse<>(entityName + " não encontrado."));
    }

    /**
     * Cria resposta de erro interno do servidor
     */
    protected <T> ResponseEntity<ApiResponse<T>> internalServerError() {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>("Erro interno do servidor."));
    }

    /**
     * Cria resposta de sucesso
     */
    protected <T> ResponseEntity<ApiResponse<T>> success(String message, T data) {
        return ResponseEntity.ok(new ApiResponse<>(message, data));
    }

    /**
     * Cria resposta de sucesso sem dados
     */
    protected ResponseEntity<ApiResponse<Void>> success(String message) {
        return ResponseEntity.ok(new ApiResponse<>(message));
    }

    /**
     * Cria resposta de erro customizada
     */
    protected <T> ResponseEntity<ApiResponse<T>> error(String message) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(message));
    }

    /**
     * Cria resposta de erro não autorizado (401)
     */
    protected <T> ResponseEntity<ApiResponse<T>> unauthorized(String message) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ApiResponse<>(message));
    }

    /**
     * Cria resposta de bad request customizada (400)
     */
    protected <T> ResponseEntity<ApiResponse<T>> badRequest(String message) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse<>(message));
    }

    /**
     * Cria resposta de not found customizada (404)
     */
    protected <T> ResponseEntity<ApiResponse<T>> notFoundCustom(String message) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse<>(message));
    }

    /**
     * Cria resposta de recurso criado com sucesso (201)
     */
    protected <T> ResponseEntity<ApiResponse<T>> created(String message, T data) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(message, data));
    }

    /**
     * Cria resposta de recurso criado sem dados (201)
     */
    protected ResponseEntity<ApiResponse<Void>> created(String message) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(message));
    }

    /**
     * Cria resposta de acesso proibido (403)
     */
    protected <T> ResponseEntity<ApiResponse<T>> forbidden(String message) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ApiResponse<>(message));
    }

    /**
     * Valida ID e lança exceção se inválido
     */
    protected void validateId(int id) {
        if (!isValidId(id)) {
            throw new IllegalArgumentException("ID deve ser maior que zero.");
        }
    }
}