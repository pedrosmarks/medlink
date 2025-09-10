package br.fai.lds.medlink.domain.dataTransferObject.Login;



import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

    // DTO usado para redefinir a senha e/ou ativar a conta, após o usuário informar o código de verificação.

    @Getter
    @Setter
    @NoArgsConstructor
    public class PasswordResetDTO {

        @NotBlank(message = "O identificador (e-mail ou CPF) é obrigatório.")
        private String identifier;

        @NotBlank(message = "O código de verificação é obrigatório.")
        private String verificationCode;

        @NotBlank(message = "A nova senha é obrigatória.")
        @Size(min = 6, max = 20, message = "A senha deve ter entre 6 e 20 caracteres.")
        private String newPassword;
    }

