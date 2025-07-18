package br.fai.lds.medlink.domain.dataTransferObject.MedicalRecord.clinical;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class CirurgiaDto {
    private String nome;
    private LocalDate data;
    private String local;
    private String observacoes;
}
