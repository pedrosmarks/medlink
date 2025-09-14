package br.fai.lds.medlink.domain.dataTransferObject.Medic;

import br.fai.lds.medlink.domain.Address;
import br.fai.lds.medlink.domain.Gender;
import br.fai.lds.medlink.domain.Medic;
import br.fai.lds.medlink.util.CpfUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

/**
 * DTO (Data Transfer Object) para criação de novos médicos no sistema.
 * <p>
 * Esta classe encapsula todos os dados necessários para o cadastro de um novo médico,
 * incluindo informações pessoais, profissionais e de contato, com validações
 * apropriadas para garantir a integridade dos dados.
 * </p>
 * 
 * @author Sistema MedLink
 * @version 1.0
 * @since 1.0
 * @see Medic
 * @see MedicResponseDto
 */
@Data
public class MedicCreateDto {

    /**
     * Nome completo do médico.
     * <p>
     * Deve conter entre 2 e 100 caracteres e não pode ser nulo.
     * Utilizado para identificação do profissional no sistema.
     * </p>
     */
    @NotNull(message = "O nome não pode ser nulo")
    @Size(min = 2, max = 100, message = "O nome deve ter entre 2 e 100 caracteres")
    private String name;

    /**
     * CPF (Cadastro de Pessoa Física) do médico.
     * <p>
     * Pode ser fornecido no formato XXX.XXX.XXX-XX ou apenas números XXXXXXXXXXX.
     * A validação aceita ambos os formatos e será normalizado internamente.
     * </p>
     */
    @NotNull(message = "O CPF não pode ser nulo")
    @Pattern(regexp = "^(\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}|\\d{11})$",
             message = "O CPF deve estar no formato XXX.XXX.XXX-XX ou conter apenas 11 dígitos")
    private String cpf;

    /**
     * Gênero do médico.
     * <p>
     * Valores possíveis definidos no enum Gender.
     * Informação utilizada para personalização e estatísticas.
     * </p>
     * 
     * @see Gender
     */
    @NotNull(message = "O gênero não pode ser nulo")
    private Gender gender;

    /**
     * Data de nascimento do médico.
     * <p>
     * Deve ser fornecida no formato dd/MM/yyyy.
     * Utilizada para cálculos de idade e validações de elegibilidade.
     * </p>
     */
    @NotNull(message = "A data de nascimento não pode ser nula")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate birthDate;

    /**
     * Número de telefone do médico.
     * <p>
     * Deve seguir o formato brasileiro com DDD: (XX) XXXXX-XXXX ou (XX) XXXX-XXXX.
     * Utilizado para contato direto com o profissional.
     * </p>
     */
    @NotNull(message = "O número de telefone não pode ser nulo")
    @Pattern(regexp = "^\\(?\\d{2}\\)?[\\s-]?\\d{4,5}[\\s-]?\\d{4}$", message = "Formato de telefone inválido")
    private String phoneNumber;

    /**
     * Endereço completo do médico.
     * <p>
     * Inclui rua, número, complemento, bairro, cidade, estado e CEP.
     * Utilizado para correspondência e localização do profissional.
     * </p>
     * 
     * @see Address
     */
    @NotNull(message = "O endereço não pode ser nulo")
    private Address address;

    /**
     * Número do CRM (Conselho Regional de Medicina) do médico.
     * <p>
     * Registro profissional obrigatório que autoriza o exercício da medicina.
     * Deve ser único no sistema e válido junto ao conselho competente.
     * </p>
     */
    @NotNull(message = "O CRM não pode ser nulo")
    private String crm;

    /**
     * Especialidade médica do profissional.
     * <p>
     * Área de atuação especializada do médico (ex: Cardiologia, Pediatria).
     * Utilizada para categorização e direcionamento de pacientes.
     * </p>
     */
    @NotNull(message = "A especialidade médica não pode ser nula")
    private String specialty;

    /**
     * Endereço de e-mail do médico.
     * <p>
     * Deve ser um e-mail válido e único no sistema.
     * Utilizado para autenticação e comunicação oficial.
     * </p>
     */
    @NotNull(message = "O endereço de e-mail não pode ser nulo")
    @Email(message = "O e-mail deve ser válido")
    private String email;

    /**
     * Senha para acesso ao sistema.
     * <p>
     * Deve conter entre 6 e 100 caracteres para garantir segurança adequada.
     * Recomenda-se o uso de combinação de letras, números e caracteres especiais.
     * Será criptografada antes do armazenamento.
     * </p>
     */
    @NotNull(message = "A senha não pode ser nula")
    @Size(min = 6, max = 100, message = "A senha deve ter entre 6 e 100 caracteres")
    private String password;

    /**
     * Converte este DTO em uma entidade Medic.
     * <p>
     * Cria uma nova instância da entidade Medic com todos os dados
     * fornecidos neste DTO, pronta para persistência no banco de dados.
     * A validação do CPF é feita durante a conversão.
     * </p>
     * 
     * @return Nova instância de Medic com os dados deste DTO
     * @throws IllegalArgumentException se o CPF não tiver o comprimento correto após normalização
     * @see Medic
     */
    public Medic toEntity() {
        // Valida o CPF antes de criar a entidade
        if (!CpfUtil.isValidLength(this.cpf)) {
            throw new IllegalArgumentException("CPF deve ter exatamente 11 dígitos");
        }

        Medic entity = new Medic();
        entity.setName(this.name);
        entity.setCpf(this.cpf); // Mantém o formato original, será normalizado no DAO
        entity.setGender(this.gender);
        entity.setBirthDate(this.birthDate);
        entity.setPhoneNumber(this.phoneNumber);
        entity.setAddress(this.address);
        entity.setCrm(this.crm);
        entity.setSpecialty(this.specialty);
        entity.setEmail(this.email);
        entity.setPassword(this.password);
        return entity;
    }
}
