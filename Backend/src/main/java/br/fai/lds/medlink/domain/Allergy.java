package br.fai.lds.medlink.domain;

import lombok.*;

// Representa uma alergia registrada no prontuário do paciente.

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Allergy {

    private int id;
    private String name;
    private String substance;
    private String reaction;
    private String severity;
}