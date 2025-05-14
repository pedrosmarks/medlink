package br.fai.lds.medlink.domain;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class Patient extends Person {


    private int id;

    @NotNull(message = "O endereco email não pode estar em branco")
    private String email;


    @NotNull(message = "O plano não pode estar em branco")
    private String plan;

    @NotNull(message = "O cartão do SUS não pode estar em branco")
    private String susCard;