package br.fai.lds.medlink.domain;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import jakarta.validation.constraints.NotNull;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public abstract class Person {

    private int id;

    @NotNull(message = "O nome não pode ser nulo")
    @Size(min = 2, max = 100, message = "O nome deve ter entre 2 e 100 caracteres")
    private String name;

    @NotNull(message = "O CPF não pode ser nulo")
    @Pattern(regexp = "^(\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2})$", message = "O CPF deve estar no formato XXX.XXX.XXX-XX")
    private String cpf;

    @NotNull(message = "O gênero não pode ser nulo")
    private Gender gender;

    @NotNull(message = "A data de nascimento não pode ser nula")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataNascimento;

    @NotNull(message = "O número de telefone não pode ser nulo")
    private String phoneNumber;

    @NotNull(message = "O endereço não pode ser nulo")
    private Address address;
}
