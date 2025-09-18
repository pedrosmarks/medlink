package br.fai.lds.medlink.domain.dataTransferObject.Access;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de resposta para solicitações de acesso ao prontuário.
 * Contém informações do médico e status da solicitação para exibição ao paciente.
 * 
 * @author MedLink Team
 * @version 1.0
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccessRequestResponseDto {
    /**
     * ID do médico que fez a solicitação.
     */
    private int medicoId;
    
    /**
     * Nome completo do médico.
     */
    private String medicoName;
    
    /**
     * Especialidade médica do profissional.
     */
    private String medicoSpecialty;
    
    /**
     * Status atual da solicitação (PENDENTE, ACEITA, RECUSADA, REVOGADA).
     */
    private String status;
}