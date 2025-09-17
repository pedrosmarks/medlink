package br.fai.lds.medlink.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO que representa um paciente autorizado para acesso.
 * 
 * <p>Contém informações básicas do paciente para exibição
 * em listas de pacientes autorizados para determinado médico.</p>
 * 
 * @author MedLink Team
 * @version 1.0
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientAuthorizedDto {
    /** ID único do paciente. */
    private int id;
    
    /** Nome completo do paciente. */
    private String name;
    
    /** Data de nascimento do paciente. */
    private LocalDate birthDate;
    
    /** Email do paciente para contato. */
    private String email;
}

