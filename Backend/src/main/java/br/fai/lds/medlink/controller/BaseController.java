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

    protected static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(BaseController.class);

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
     * Executa operação com tratamento de exceções padronizado
     */
    protected <T> ResponseEntity<ApiResponse<T>> executeWithErrorHandling(
            java.util.function.Supplier<ResponseEntity<ApiResponse<T>>> operation,
            String errorContext) {
        try {
            return operation.get();
        } catch (IllegalArgumentException e) {
            log.warn("Erro de validação em {}: {}", errorContext, LogSanitizer.sanitize(e.getMessage()));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>("Dados inválidos: " + e.getMessage()));
        } catch (Exception e) {
            log.error("Erro em {}: {}", errorContext, LogSanitizer.sanitize(e.getMessage()), e);
            return internalServerError();
        }
    }

    /**
     * Valida ID e retorna erro se inválido
     */
    protected <T> ResponseEntity<ApiResponse<T>> validateId(int id) {
        if (!isValidId(id)) {
            return badRequestInvalidId();
        }
        return null; // ID válido
    }
}