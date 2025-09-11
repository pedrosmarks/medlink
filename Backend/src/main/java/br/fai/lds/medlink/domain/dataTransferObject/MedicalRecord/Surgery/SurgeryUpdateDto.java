package br.fai.lds.medlink.domain.dataTransferObject.MedicalRecord.Surgery;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class SurgeryUpdateDto {
    
    @Size(min = 2, max = 100, message = "Nome da cirurgia deve ter entre 2 e 100 caracteres")
    private String name;
    
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate date;
    
    @Size(min = 2, max = 100, message = "Local deve ter entre 2 e 100 caracteres")
    private String location;
    
    @Size(max = 500, message = "Observações devem ter no máximo 500 caracteres")
    private String notes;
}