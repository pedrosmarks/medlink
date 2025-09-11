package br.fai.lds.medlink.domain.dataTransferObject.Login;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * DTO (Data Transfer Object) para resposta de autenticação bem-sucedida.
 * <p>
 * Esta classe encapsula as informações do usuário que são retornadas após
 * um login bem-sucedido, incluindo dados básicos de identificação e perfil.
 * </p>
 * 
 * @author Sistema MedLink
 * @version 1.0
 * @since 1.0
 */
@Getter
@AllArgsConstructor
public class LoginResponseDTO {
    
    /**
     * Identificador único do usuário no sistema.
     * <p>
     * Utilizado para referênciar o usuário em operações subsequentes
     * e manter a sessão ativa.
     * </p>
     */
    private Integer id;
    
    /**
     * Nome completo do usuário autenticado.
     * <p>
     * Utilizado para personalização da interface e identificação
     * do usuário logado no sistema.
     * </p>
     */
    private String name;
    
    /**
     * Perfil/tipo do usuário no sistema.
     * <p>
     * Define as permissões e funcionalidades disponíveis para o usuário.
     * Valores possíveis: "MEDICO", "PACIENTE", "ADMIN", etc.
     * </p>
     */
    private String profile;
}
