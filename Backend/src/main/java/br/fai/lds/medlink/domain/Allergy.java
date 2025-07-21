package br.fai.lds.medlink.domain;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Allergy {

    private String name;
    private String substance;
    private String reaction;
    private String severity;

}