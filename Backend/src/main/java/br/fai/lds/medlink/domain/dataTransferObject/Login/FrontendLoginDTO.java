package br.fai.lds.medlink.domain.dataTransferObject.Login;

import lombok.Data;

@Data
public class FrontendLoginDTO {
    private String usuario;
    private String senha;
}