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
public class Medic extends Person {

    private int id;

    @NotNull(message = "O crm não pode estar em branco")
    private String crm;


    @NotNull(message = "A especialidade médica não pode estar em branco")
    private String specialty;

    @NotNull(message = "O endereço de email não pode estar em branco")
    private String email;

    private boolean active = true;
}