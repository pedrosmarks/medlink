package br.fai.lds.medlink.domain;

import lombok.*;

//Representa uma cirurgia realizada pelo paciente.

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Surgery {
    private String name;
    private String date;
    private String location;
    private String notes;
}