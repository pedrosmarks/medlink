package br.fai.lds.medlink.domain;

import lombok.*;

/**
 * Entidade que representa uma alergia no prontuário médico.
 * 
 * <p>Registra informações detalhadas sobre alergias do paciente,
 * incluindo substância, reação e grau de severidade.</p>
 * 
 * @author MedLink Team
 * @version 1.0
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Allergy {

    /** ID único da alergia. */
    private int id;
    
    /** Nome da alergia. */
    private String name;
    
    /** Substância que causa a reação alérgica. */
    private String substance;
    
    /** Tipo de reação alérgica apresentada. */
    private String reaction;
    
    /** Grau de severidade da alergia (leve, moderada, grave). */
    private String severity;
}