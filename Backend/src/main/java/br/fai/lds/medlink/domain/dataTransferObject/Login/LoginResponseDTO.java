package br.fai.lds.medlink.domain.dataTransferObject.Login;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponseDTO {
    private Integer id;
    private String name;
    private String profile;
}
