package br.fai.lds.medlink.domain.dataTransferObject.Login;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * DTO (Data Transfer Object) para requisições de login no sistema.
 * <p>
 * Esta classe encapsula as credenciais necessárias para autenticação de usuários,
 * incluindo validações de formato e obrigatoriedade dos campos.
 * </p>
 * 
 * @author Sistema MedLink
 * @version 1.0
 * @since 1.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginDTO {

    /**
     * Endereço de e-mail do usuário para autenticação.
     * <p>
     * Deve ser um e-mail válido e não pode estar em branco.
     * Utilizado como identificador único do usuário no sistema.
     * </p>
     */
    @Email(message = "E-mail inválido")
    @NotBlank(message = "E-mail é obrigatório")
    private String email;

    /**
     * Senha do usuário para autenticação.
     * <p>
     * Deve conter entre 1 e 20 caracteres e não pode estar em branco.
     * Recomenda-se o uso de senhas seguras com combinação de letras, números e símbolos.
     * </p>
     */
    @NotBlank(message = "Senha é obrigatória")
    @Size(min = 1, max = 20, message = "Senha deve ter entre 1 e 20 caracteres")
    private String password;

    /**
     * Tipo de usuário para autenticação.
     * <p>
     * Deve ser "MEDIC" para médicos ou "PATIENT" para pacientes.
     * Este campo determina em qual tabela será feita a autenticação.
     * </p>
     */
    @NotBlank(message = "Tipo de usuário é obrigatório")
    private String userType;
}
