package br.fai.lds.medlink.domain;

import lombok.*;

/**
 * Entidade que representa um medicamento no prontuário médico.
 * 
 * <p>Registra informações sobre medicações prescritas ou utilizadas
 * pelo paciente, incluindo dosagem e frequência de uso.</p>
 * 
 * @author MedLink Team
 * @version 1.0
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Medication {
    /** ID único do medicamento. */
    private Integer id;
    
    /** Nome do medicamento. */
    private String name;
    
    /** Dosagem prescrita do medicamento. */
    private String dosage;
    
    /** Frequência de administração do medicamento. */
    private String frequency;

    /** Indica se o registro do medicamento foi excluído logicamente. */
    private boolean softDeleted;
    public boolean isSoftDeleted() { return softDeleted; }
    public void setSoftDeleted(boolean softDeleted) { this.softDeleted = softDeleted; }
}
