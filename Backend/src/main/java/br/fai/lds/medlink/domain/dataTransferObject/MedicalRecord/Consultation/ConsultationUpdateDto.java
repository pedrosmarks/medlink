package br.fai.lds.medlink.domain.dataTransferObject.MedicalRecord.Consultation;

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
public class ConsultationUpdateDto {
    
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate date;
    
    @Size(min = 5, max = 200, message = "Motivo da consulta deve ter entre 5 e 200 caracteres")
    private String reason;
    
    @Size(max = 500, message = "Observações devem ter no máximo 500 caracteres")
    private String notes;
}