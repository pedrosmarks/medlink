package br.fai.lds.medlink.domain;

import lombok.*;
import java.time.LocalDate;

/**
 * Entidade que representa uma cirurgia no prontuário médico.
 * 
 * <p>Registra informações sobre cirurgias realizadas pelo paciente,
 * incluindo data, local e observações médicas.</p>
 * 
 * @author MedLink Team
 * @version 1.0
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Surgery {
    /** ID único da cirurgia. */
    private int id;
    
    /** Nome ou tipo da cirurgia realizada. */
    private String name;
    
    /** Data em que a cirurgia foi realizada. */
    private LocalDate date;
    
    /** Local onde a cirurgia foi realizada. */
    private String location;
    
    /** Observações e anotações sobre a cirurgia. */
    private String notes;

    /** Indica se a cirurgia foi marcada como deletada (soft delete). */
    private boolean softDeleted;
    public boolean isSoftDeleted() { return softDeleted; }
    public void setSoftDeleted(boolean softDeleted) { this.softDeleted = softDeleted; }
}