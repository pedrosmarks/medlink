package br.fai.lds.medlink.domain;

import lombok.*;
import java.time.LocalDate;

// Representa um diagnóstico no prontuário do paciente

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Diagnosis {
    private int id;
    private String description;
    private LocalDate date;
}
