package br.fai.lds.medlink.domain.dataTransferObject.Access;

import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para solicitação de acesso ao prontuário do paciente.
 * Utilizado quando um médico solicita acesso aos dados médicos de um paciente.
 * 
 * @author MedLink Team
 * @version 1.0
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccessRequestDto {
    
    /**
     * ID do médico que está solicitando acesso ao prontuário.
     */
    @NotNull(message = "ID do médico é obrigatório")
    private int medicoId;
    
    /**
     * Status da solicitação (PENDENTE, ACEITA, RECUSADA, REVOGADA).
     * Automaticamente convertido para maiúsculas.
     */
    private String status;
    
    /**
     * Obtém o status da solicitação em maiúsculas.
     * 
     * @return status normalizado em maiúsculas ou null
     */
    public String getStatus() {
        return status != null ? status.toUpperCase() : null;
    }
    
    /**
     * Define o status da solicitação, convertendo automaticamente para maiúsculas.
     * 
     * @param status o status da solicitação
     */
    public void setStatus(String status) {
        this.status = status != null ? status.toUpperCase() : null;
    }
}