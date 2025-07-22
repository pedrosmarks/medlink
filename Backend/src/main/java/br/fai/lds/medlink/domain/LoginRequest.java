package br.fai.lds.medlink.domain;

import lombok.Getter;
import lombok.Setter;


// Representa os dados de entrada para autenticação do usuário.
@Getter
@Setter
public class LoginRequest {
    private String email;
    private String password;
}

