package br.fai.lds.medlink.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientAuthorizedDto {
    private int id;
    private String name;
    private LocalDate birthDate;
    private String email;
}

