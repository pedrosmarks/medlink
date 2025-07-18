package br.fai.lds.medlink.domain.dataTransferObject.MedicalRecord.clinical;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AlergiaDto {
    private String substancia;
    private String reacao;
}

