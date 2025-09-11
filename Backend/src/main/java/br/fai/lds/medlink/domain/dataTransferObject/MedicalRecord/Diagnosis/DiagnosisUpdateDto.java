package br.fai.lds.medlink.domain.dataTransferObject.MedicalRecord.Diagnosis;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiagnosisUpdateDto {
    
    @Size(min = 5, max = 500, message = "Descrição do diagnóstico deve ter entre 5 e 500 caracteres")
    private String description;
    
    @Size(min = 8, max = 10, message = "Data deve estar no formato dd/MM/yyyy")
    private String date;
}