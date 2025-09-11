package br.fai.lds.medlink.domain.dataTransferObject.Login;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO (Data Transfer Object) para redefinição de senha com código de verificação.
 * <p>
 * Esta classe é utilizada na segunda etapa do processo de recuperação de senha,
 * onde o usuário informa o código de verificação recebido e define uma nova senha.
 * Também pode ser usado para ativação de conta após o cadastro inicial.
 * </p>
 * 
 * @author Sistema MedLink
 * @version 1.0
 * @since 1.0
 */
@Getter
@Setter
@NoArgsConstructor
public class PasswordResetDTO {

    /**
     * Identificador do usuário (e-mail ou CPF).
     * <p>
     * Deve corresponder ao identificador utilizado na solicitação inicial
     * de recuperação de senha. Utilizado para localizar o usuário no sistema.
     * </p>
     */
    @NotBlank(message = "O identificador (e-mail ou CPF) é obrigatório.")
    private String identifier;

    /**
     * Código de verificação enviado para o usuário.
     * <p>
     * Código numérico ou alfanumérico gerado pelo sistema e enviado
     * via e-mail ou SMS para confirmar a identidade do usuário.
     * </p>
     */
    @NotBlank(message = "O código de verificação é obrigatório.")
    private String verificationCode;

    /**
     * Nova senha a ser definida para o usuário.
     * <p>
     * Deve conter entre 6 e 20 caracteres para garantir segurança adequada.
     * Recomenda-se o uso de combinação de letras maiúsculas, minúsculas,
     * números e caracteres especiais.
     * </p>
     */
    @NotBlank(message = "A nova senha é obrigatória.")
    @Size(min = 6, max = 20, message = "A senha deve ter entre 6 e 20 caracteres.")
    private String newPassword;
}

