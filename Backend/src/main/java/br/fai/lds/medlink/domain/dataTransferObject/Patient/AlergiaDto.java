package br.fai.lds.medlink.domain.dataTransferObject.Patient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlergiaDto {
    private String name;
    private String substance;
    private String reaction;
    private String severity;
}