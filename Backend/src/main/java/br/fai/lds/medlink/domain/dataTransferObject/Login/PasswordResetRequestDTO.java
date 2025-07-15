package br.fai.lds.medlink.domain.dataTransferObject.Login;


import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

    //DTO usado quando o usuário solicita recuperação de senh.O identificador e o e-mail.

    @Getter
    @Setter
    @NoArgsConstructor
    public class PasswordResetRequestDTO {

        @NotBlank(message = "Informe o e-mail ou CPF para recuperação de conta.")
        private String identifier;
    }

