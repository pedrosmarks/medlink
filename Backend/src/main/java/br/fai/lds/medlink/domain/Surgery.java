package br.fai.lds.medlink.domain;

import lombok.*;
import java.time.LocalDate;

// Representa uma cirurgia realizada pelo paciente.

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Surgery {
    private int id;
    private String name;
    private LocalDate date;
    private String location;
    private String notes;
}