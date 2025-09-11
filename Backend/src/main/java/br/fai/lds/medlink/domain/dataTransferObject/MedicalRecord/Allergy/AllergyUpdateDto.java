package br.fai.lds.medlink.domain.dataTransferObject.MedicalRecord.Allergy;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AllergyUpdateDto {
    
    @Size(min = 2, max = 100, message = "Nome da alergia deve ter entre 2 e 100 caracteres")
    private String name;
    
    @Size(min = 2, max = 100, message = "Substância deve ter entre 2 e 100 caracteres")
    private String substance;
    
    @Size(min = 2, max = 200, message = "Reação deve ter entre 2 e 200 caracteres")
    private String reaction;
    
    @Size(min = 2, max = 50, message = "Severidade deve ter entre 2 e 50 caracteres")
    private String severity;
}