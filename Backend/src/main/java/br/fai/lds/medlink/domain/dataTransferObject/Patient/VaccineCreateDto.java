package br.fai.lds.medlink.domain.dataTransferObject.Patient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VaccineCreateDto {
    private String name;
    private String date;
}