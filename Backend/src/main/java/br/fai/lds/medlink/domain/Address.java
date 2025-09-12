package br.fai.lds.medlink.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

//Representa o endereço de um paciente ou médico no sistema.
public class Address {

    @NotBlank(message = "Nome da rua não pode estar em branco")
    private String street;

    @Size(min = 1, message = "Numero deve ter pelo menos 1 caractere")
    private String number;

    private String complement;

    @NotBlank(message = "Bairro não pode estar em branco")
    private String neighborhood;

    @NotBlank(message = "Cidade não pode estar em branco")
    private String city;

    @NotBlank(message = "Estado não pode estar em branco")
    private String state;

    @Pattern(regexp = "^\\d{5}-\\d{3}$", message = "CEP deve estar no formato XXXXX-XXX")
    private String zipCode;

}