package br.fai.lds.medlink.domain.dataTransferObject.Login;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO (Data Transfer Object) para solicitação de recuperação de senha.
 * <p>
 * Esta classe é utilizada na primeira etapa do processo de recuperação de senha,
 * onde o usuário informa seu identificador (e-mail ou CPF) para receber
 * um código de verificação que permitirá a redefinição da senha.
 * </p>
 * 
 * @author Sistema MedLink
 * @version 1.0
 * @since 1.0
 */
@Getter
@Setter
@NoArgsConstructor
public class PasswordResetRequestDTO {

    /**
     * Identificador do usuário para recuperação de senha.
     * <p>
     * Pode ser o endereço de e-mail ou CPF cadastrado no sistema.
     * Utilizado para localizar o usuário e enviar o código de verificação
     * necessário para a redefinição da senha.
     * </p>
     * 
     * @see PasswordResetDTO
     */
    @NotBlank(message = "Informe o e-mail ou CPF para recuperação de conta.")
    private String identifier;
}

