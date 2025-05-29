package br.fai.lds.medlink.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class Address {

    @NotBlank(message = "Nome da rua não pode estar em branco")
    private final String street;

    @Size(min = 8, message = "Numero deve ter pelo menos 1 caractere")
    private final String number;

    private final String complement;

    @NotBlank(message = "Bairro não pode estar em branco")
    private final String neighborhood;

    @NotBlank(message = "Cidade não pode estar em branco")
    private final String city;

    @NotBlank(message = "Estado não pode estar em branco")
    private final String state;

    @Pattern(regexp = "^\\d{5}-\\d{3}$", message = "CEP deve estar no formato XXXXX-XXX")
    private final String zipCode;

}