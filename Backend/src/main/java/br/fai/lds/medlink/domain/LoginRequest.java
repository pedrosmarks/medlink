package br.fai.lds.medlink.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * Entidade que representa uma solicitação de login no sistema.
 * 
 * <p>Contém as credenciais necessárias para autenticação
 * de usuários (pacientes e médicos) no sistema.</p>
 * 
 * @author MedLink Team
 * @version 1.0
 * @since 1.0
 */
@Getter
@Setter
public class LoginRequest {
    /** Email do usuário para autenticação. */
    private String email;
    
    /** Senha do usuário para autenticação. */
    private String password;
}

