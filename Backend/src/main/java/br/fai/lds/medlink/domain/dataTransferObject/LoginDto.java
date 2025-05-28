package br.fai.lds.medlink.domain.dataTransferObject;

import lombok.Data;

@Data
public class LoginDto {

    private int id;
    private String email;
    private String password;
}
