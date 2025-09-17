package br.fai.lds.medlink.domain.dataTransferObject.MedicalRecord.Medication;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para atualização de dados de medicações existentes.
 * 
 * <p>Permite atualização parcial dos dados de medicação, onde apenas
 * os campos não nulos serão atualizados na entidade existente.</p>
 * 
 * @author MedLink Team
 * @version 1.0
 * @since 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedicationUpdateDto {
    
    /**
     * Novo nome do medicamento (opcional).
     */
    @Size(min = 2, max = 100, message = "Nome do medicamento deve ter entre 2 e 100 caracteres")
    private String name;
    
    /**
     * Nova dosagem do medicamento (opcional).
     */
    @Size(min = 2, max = 50, message = "Dosagem deve ter entre 2 e 50 caracteres")
    private String dosage;
    
    /**
     * Nova frequência de administração (opcional).
     */
    @Size(min = 2, max = 100, message = "Frequência deve ter entre 2 e 100 caracteres")
    private String frequency;
}