package br.fai.lds.medlink.domain.dataTransferObject.MedicalRecord.clinical;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MedicamentoDto {
    private String nome;
    private String dosagem;
    private String frequencia;
}

