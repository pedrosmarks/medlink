package br.fai.lds.medlink.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidade que representa um especialista autorizado a acessar o prontuário.
 * 
 * <p>Mantém o registro de médicos que possuem autorização para
 * visualizar e editar informações do prontuário do paciente.</p>
 * 
 * @author MedLink Team
 * @version 1.0
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EspecialistaAutorizado {
    /** ID do médico autorizado a acessar o prontuário. */
    private Long medicoId;
}