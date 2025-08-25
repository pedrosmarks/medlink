package br.fai.lds.medlink.domain.dataTransferObject.Patient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CirurgiaDto {
    private int id;
    private String name;
    private String date;
    private String location;
    private String notes;
}