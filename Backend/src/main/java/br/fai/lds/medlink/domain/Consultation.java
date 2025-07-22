package br.fai.lds.medlink.domain;

import lombok.*;


//Representa uma consulta médica registrada no prontuário do paciente
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Consultation {
    private String date;
    private String reason;
    private String notes;
}
