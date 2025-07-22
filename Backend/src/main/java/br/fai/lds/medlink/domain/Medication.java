package br.fai.lds.medlink.domain;

import lombok.*;

// Representa um medicamento prescrito ou utilizado por um paciente.

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Medication {
    private String name;
    private String dosage;
    private String frequency;
}
