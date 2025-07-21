package br.fai.lds.medlink.domain;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Consultation {
    private String date;
    private String reason;
    private String notes;
}
