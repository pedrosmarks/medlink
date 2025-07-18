package br.fai.lds.medlink.domain.dataTransferObject.MedicalRecord.clinical;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class DiagnosticoDto {
    private String descricao;
    private LocalDate data;
}

