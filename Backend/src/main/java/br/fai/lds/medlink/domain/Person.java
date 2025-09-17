package br.fai.lds.medlink.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import jakarta.validation.constraints.NotNull;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import java.time.LocalDate;

/**
 * Classe abstrata que representa uma pessoa genérica no sistema.
 * 
 * <p>Contém os dados básicos comuns a todas as pessoas (pacientes, médicos, etc.),
 * como informações pessoais, documentos e dados de contato.</p>
 * 
 * @author MedLink Team
 * @version 1.0
 * @since 1.0
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public abstract class Person {

    /** ID único da pessoa no sistema. */
    private int id;

    /** Nome completo da pessoa. */
    @NotNull(message = "O nome não pode ser nulo")
    @Size(min = 2, max = 100, message = "O nome deve ter entre 2 e 100 caracteres")
    private String name;

    /** CPF da pessoa no formato XXX.XXX.XXX-XX. */
    @NotNull(message = "O CPF não pode ser nulo")
    @Pattern(regexp = "^(\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2})$", message = "O CPF deve estar no formato XXX.XXX.XXX-XX")
    private String cpf;

    /** Senha de acesso ao sistema. */
    @Size(min = 6, max = 100, message = "A senha deve ter entre 6 e 100 caracteres")
    private String password;

    /** Gênero da pessoa (MASCULINO, FEMININO, OUTRO). */
    @NotNull(message = "O gênero não pode ser nulo")
    private Gender gender;

    /** Data de nascimento da pessoa. */
    @NotNull(message = "A data de nascimento não pode ser nula")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate birthDate;

    /** Número de telefone para contato. */
    @NotNull(message = "O número de telefone não pode ser nulo")
    private String phoneNumber;

    /** Endereço residencial da pessoa. */
    @NotNull(message = "O endereço não pode ser nulo")
    private Address address;

}
