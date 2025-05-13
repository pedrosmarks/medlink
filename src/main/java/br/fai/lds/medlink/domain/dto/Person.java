package br.fai.lds.medlink.domain.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Person {

    private int id;

    @NotNull(message = "O nome não pode ser nulo")
    @Size(min = 2, max = 100, message = "O nome deve ter entre 2 e 100 caracteres")
    private String name;

    private String cpf;
    private Gender gender;
    private LocalDate dataNascimento;
    private String phoneNumber;
    private Address address;
}
