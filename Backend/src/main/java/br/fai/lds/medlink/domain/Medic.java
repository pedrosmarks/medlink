package br.fai.lds.medlink.domain;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Entidade que representa um médico no sistema MedLink.
 * 
 * <p>Extende a classe Person e adiciona informações específicas da profissão médica,
 * como CRM, especialidade e status de atividade no sistema.</p>
 * 
 * @author MedLink Team
 * @version 1.0
 * @since 1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class Medic extends Person {

    /** ID único do médico no sistema. */
    private int id;

    /** Número do Conselho Regional de Medicina (CRM). */
    @NotNull(message = "O crm não pode estar em branco")
    private String crm;

    /** Especialidade médica do profissional. */
    @NotNull(message = "A especialidade médica não pode estar em branco")
    private String specialty;

    /** Email profissional do médico. */
    @NotNull(message = "O endereço de email não pode estar em branco")
    private String email;

    /** Indica se o médico está ativo no sistema. */
    @Builder.Default
    private boolean active = true;
}