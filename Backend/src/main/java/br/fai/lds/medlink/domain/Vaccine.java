package br.fai.lds.medlink.domain;

import lombok.*;
import java.time.LocalDate;

/**
 * Entidade que representa uma vacina no prontuário médico.
 * 
 * <p>Registra informações sobre vacinas aplicadas ao paciente,
 * mantendo o histórico de imunização completo.</p>
 * 
 * @author MedLink Team
 * @version 1.0
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vaccine {
    /** ID único da vacina. */
    private int id;
    
    /** Nome da vacina aplicada. */
    private String name;
    
    /** Data de aplicação da vacina. */
    private LocalDate date;
}
