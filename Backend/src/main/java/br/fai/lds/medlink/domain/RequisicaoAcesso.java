package br.fai.lds.medlink.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidade que representa uma solicitação de acesso ao prontuário.
 * 
 * <p>Registra solicitações de médicos para acessar prontuários
 * de pacientes, incluindo o status da solicitação.</p>
 * 
 * @author MedLink Team
 * @version 1.0
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequisicaoAcesso {
    /** ID do médico que solicita acesso. */
    private int medicoId;
    
    /** Status da solicitação (PENDENTE, ACEITA, RECUSADA, REVOGADA). */
    private String status;
}