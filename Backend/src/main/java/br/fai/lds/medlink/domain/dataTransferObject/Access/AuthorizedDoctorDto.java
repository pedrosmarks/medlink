package br.fai.lds.medlink.domain.dataTransferObject.Access;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para médicos autorizados a acessar o prontuário do paciente.
 * Utilizado para listar os profissionais que já possuem acesso aprovado aos dados médicos.
 * 
 * @author MedLink Team
 * @version 1.0
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorizedDoctorDto {
    /**
     * ID único do médico no sistema.
     */
    private int id;
    
    /**
     * Nome completo do médico autorizado.
     */
    private String name;
    
    /**
     * Especialidade médica do profissional.
     */
    private String specialty;
    
    /**
     * Número do Conselho Regional de Medicina (CRM).
     */
    private String crm;
}