package br.fai.lds.medlink.domain;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Medication {
    private String name;
    private String dosage;
    private String frequency;
}
