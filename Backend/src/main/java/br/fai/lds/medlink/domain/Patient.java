package br.fai.lds.medlink.domain;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;


@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class Patient extends Person {


    private int id;

    @NotNull(message = "O endereco de email não pode estar em branco")
    private String email;


    @NotNull(message = "O plano não pode estar em branco")
    private String plan;

    @NotNull(message = "O cartão do SUS não pode estar em branco")
    private String susCard;

    @Builder.Default
    private boolean active = true;

    private int medicId;

}