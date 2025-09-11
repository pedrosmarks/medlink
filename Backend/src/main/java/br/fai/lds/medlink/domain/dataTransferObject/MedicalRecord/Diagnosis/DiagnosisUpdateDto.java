package br.fai.lds.medlink.domain.dataTransferObject.MedicalRecord.Diagnosis;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiagnosisUpdateDto {
    
    @Size(min = 5, max = 500, message = "Descrição do diagnóstico deve ter entre 5 e 500 caracteres")
    private String description;
    
    @JsonFormat(pattern = "dd/MM/yyyy")
    @PastOrPresent(message = "Data do diagnóstico não pode ser futura")
    private LocalDate date;
}