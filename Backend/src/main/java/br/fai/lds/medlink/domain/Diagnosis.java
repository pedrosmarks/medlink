package br.fai.lds.medlink.domain;

import lombok.*;


//Representa uma diagnostico no prontuário do paciente
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Diagnosis {
    private int id;
    private String description;
    private String date;

}
