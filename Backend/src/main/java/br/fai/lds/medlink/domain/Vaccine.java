package br.fai.lds.medlink.domain;

import lombok.*;
import java.time.LocalDate;

// Representa uma vacina aplicada ao paciente

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vaccine {
    private int id;
    private String name;
    private LocalDate date;
}
