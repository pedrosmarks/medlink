package br.fai.lds.medlink.domain;

import lombok.*;
import java.time.LocalDate;

/**
 * Entidade que representa um diagnóstico médico no prontuário.
 * 
 * <p>Registra diagnósticos realizados pelos profissionais de saúde,
 * incluindo descrição detalhada e data do diagnóstico.</p>
 * 
 * @author MedLink Team
 * @version 1.0
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Diagnosis {
    /** ID único do diagnóstico. */
    private int id;
    
    /** Descrição detalhada do diagnóstico médico. */
    private String description;
    
    /** Data em que o diagnóstico foi realizado. */
    private LocalDate date;

    /** Indica se o diagnóstico foi excluído logicamente. */
    private boolean softDeleted;
    public boolean isSoftDeleted() { return softDeleted; }
    public void setSoftDeleted(boolean softDeleted) { this.softDeleted = softDeleted; }
}
