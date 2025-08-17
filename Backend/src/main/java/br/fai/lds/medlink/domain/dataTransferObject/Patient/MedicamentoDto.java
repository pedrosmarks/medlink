package br.fai.lds.medlink.domain.dataTransferObject.Patient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicamentoDto {
    private String nome;
    private String dosagem;
    private String inicio; // pode ser null
}