package br.fai.lds.medlink.domain.dataTransferObject.MedicalRecord.Allergy;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para atualização de dados de alergias existentes.
 * 
 * <p>Permite atualização parcial dos dados de alergia, onde apenas
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
public class AllergyUpdateDto {
    
    /**
     * Novo nome da alergia (opcional).
     */
    @Size(min = 2, max = 100, message = "Nome da alergia deve ter entre 2 e 100 caracteres")
    private String name;
    
    /**
     * Nova substância alergênica (opcional).
     */
    @Size(min = 2, max = 100, message = "Substância deve ter entre 2 e 100 caracteres")
    private String substance;
    
    /**
     * Nova reação alérgica (opcional).
     */
    @Size(min = 2, max = 200, message = "Reação deve ter entre 2 e 200 caracteres")
    private String reaction;
    
    /**
     * Nova severidade da alergia (opcional).
     */
    @Size(min = 2, max = 50, message = "Severidade deve ter entre 2 e 50 caracteres")
    private String severity;
}