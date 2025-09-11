package br.fai.lds.medlink.domain.dataTransferObject.MedicalRecord.Vaccine;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
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
public class VaccineUpdateDto {
    
    @Size(min = 2, max = 100, message = "Nome da vacina deve ter entre 2 e 100 caracteres")
    private String name;
    
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate date;
}