package br.fai.lds.medlink.domain;

import lombok.*;
import java.time.LocalDate;


//Representa uma consulta médica registrada no prontuário do paciente
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Consultation {
    private LocalDate date;
    private String reason;
    private String notes;
}
