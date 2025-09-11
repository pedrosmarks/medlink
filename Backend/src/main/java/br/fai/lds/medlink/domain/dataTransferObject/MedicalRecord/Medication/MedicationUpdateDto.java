package br.fai.lds.medlink.domain.dataTransferObject.MedicalRecord.Medication;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedicationUpdateDto {
    
    @Size(min = 2, max = 100, message = "Nome do medicamento deve ter entre 2 e 100 caracteres")
    private String name;
    
    @Size(min = 2, max = 50, message = "Dosagem deve ter entre 2 e 50 caracteres")
    private String dosage;
    
    @Size(min = 2, max = 100, message = "Frequência deve ter entre 2 e 100 caracteres")
    private String frequency;
}