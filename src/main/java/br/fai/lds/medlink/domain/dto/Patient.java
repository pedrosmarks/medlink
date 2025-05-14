package br.fai.lds.medlink.domain.dto;

import jakarta.validation.constraints.NotNull;

public class Patient extends Person{


    private int id;

    @NotNull(message = "O endereco email não pode estar em branco")
    private String email;


    @NotNull(message = "O plano não pode estar em branco")
    private String plan;

    @NotNull(message = "O cartão do SUS não pode estar em branco")
    private String susCard;
}
