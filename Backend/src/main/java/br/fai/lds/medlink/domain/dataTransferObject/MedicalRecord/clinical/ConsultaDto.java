package br.fai.lds.medlink.domain.dataTransferObject.MedicalRecord.clinical;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class ConsultaDto {
    private LocalDate data;
    private String motivo;
    private String observacoes;
}

