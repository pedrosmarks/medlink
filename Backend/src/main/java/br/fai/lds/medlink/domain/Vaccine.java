package br.fai.lds.medlink.domain;

import lombok.*;

// Representa uma vacina aplicada ao paciente

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vaccine {
    private int id;
    private String name;
    private String date;
}
