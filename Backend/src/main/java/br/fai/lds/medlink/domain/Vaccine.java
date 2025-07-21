package br.fai.lds.medlink.domain;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vaccine {
    private String name;
    private String date;
}
