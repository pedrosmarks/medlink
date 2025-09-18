package br.fai.lds.medlink.domain.dataTransferObject.Medic;

import br.fai.lds.medlink.domain.Address;
import br.fai.lds.medlink.domain.Gender;
import br.fai.lds.medlink.domain.Medic;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

/**
 * DTO (Data Transfer Object) para atualização de dados do médico.
 * <p>
 * Esta classe permite a atualização parcial dos dados do médico, onde apenas
 * os campos fornecidos (não nulos) serão atualizados na entidade existente.
 * Todos os campos são opcionais, permitindo atualizações granulares.
 * </p>
 * 
 * @author Sistema MedLink
 * @version 1.0
 * @since 1.0
 * @see Medic
 * @see MedicCreateDto
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MedicUpdateDto {

    /**
     * Nome completo do médico (opcional).
     * <p>
     * Se fornecido, deve conter entre 2 e 100 caracteres.
     * </p>
     */
    @Size(min = 2, max = 100, message = "O nome deve ter entre 2 e 100 caracteres")
    private String name;

    /**
     * CPF do médico (opcional).
     * <p>
     * Se fornecido, deve estar no formato XXX.XXX.XXX-XX.
     * </p>
     */
    @Pattern(regexp = "^(\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2})$", message = "O CPF deve estar no formato XXX.XXX.XXX-XX")
    private String cpf;

    /**
     * Gênero do médico (opcional).
     * 
     * @see Gender
     */
    private Gender gender;

    /**
     * Data de nascimento do médico (opcional).
     * <p>
     * Aceita formato: yyyy-MM-dd.
     * </p>
     */
    @com.fasterxml.jackson.annotation.JsonFormat(shape = com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    /**
     * Número de telefone do médico (opcional).
     * <p>
     * Se fornecido, deve seguir o formato brasileiro com DDD.
     * </p>
     */
    @Pattern(regexp = "^\\(?\\d{2}\\)?[\\s-]?\\d{4,5}[\\s-]?\\d{4}$", message = "Formato de telefone inválido")
    private String phoneNumber;

    /**
     * Endereço completo do médico (opcional).
     * 
     * @see Address
     */
    private Address address;

    /**
     * Número do CRM do médico (opcional).
     * <p>
     * Se fornecido, deve estar no formato NNNN/UF ou NNNNNN/UF.
     * </p>
     */
    @Pattern(regexp = "^\\d{4,6}/[A-Z]{2}$", message = "CRM deve estar no formato NNNN/UF ou NNNNNN/UF")
    private String crm;

    /**
     * Especialidade médica do profissional (opcional).
     * <p>
     * Se fornecida, deve conter entre 2 e 100 caracteres.
     * </p>
     */
    @Size(min = 2, max = 100, message = "A especialidade deve ter entre 2 e 100 caracteres")
    private String specialty;

    /**
     * Endereço de e-mail do médico (opcional).
     * <p>
     * Se fornecido, deve ser um e-mail válido.
     * </p>
     */
    @Email(message = "O e-mail deve ser válido")
    private String email;

    /**
     * Status de ativação do médico (opcional).
     * <p>
     * true = ativo, false = inativo/desabilitado, null = não alterar
     * </p>
     */
    private Boolean active;

    /**
     * Atualiza uma entidade Medic com os dados fornecidos neste DTO.
     * <p>
     * Apenas os campos não nulos deste DTO serão aplicados à entidade,
     * permitindo atualizações parciais sem afetar campos não especificados.
     * </p>
     * 
     * @param entity Entidade Medic a ser atualizada
     * @throws NullPointerException se entity for null
     * @see Medic
     */
    public void updateEntity(Medic entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entidade Medic não pode ser null");
        }
        try {
            // Normaliza o CPF para apenas números antes de atualizar a entidade
            if (this.cpf != null) {
                String cpfNumeros = this.cpf.replaceAll("\\D", "");
                entity.setCpf(cpfNumeros);
            }
            updateBasicInfo(entity);
            updateContactInfo(entity);
            updateProfessionalInfo(entity);
            updateStatus(entity);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao atualizar entidade Medic: " + e.getMessage(), e);
        }
    }

    /**
     * Atualiza informações básicas do médico.
     * 
     * @param entity Entidade a ser atualizada
     */
    private void updateBasicInfo(Medic entity) {
        if (this.name != null) entity.setName(this.name);
        if (this.gender != null) entity.setGender(this.gender);
        if (this.birthDate != null) entity.setBirthDate(this.birthDate);
    }

    /**
     * Atualiza informações de contato do médico.
     * 
     * @param entity Entidade a ser atualizada
     */
    private void updateContactInfo(Medic entity) {
        if (this.phoneNumber != null) entity.setPhoneNumber(this.phoneNumber);
        if (this.address != null) entity.setAddress(this.address);
        if (this.email != null) entity.setEmail(this.email);
    }

    /**
     * Atualiza informações profissionais do médico.
     * 
     * @param entity Entidade a ser atualizada
     */
    private void updateProfessionalInfo(Medic entity) {
        if (this.crm != null) entity.setCrm(this.crm);
        if (this.specialty != null) entity.setSpecialty(this.specialty);
    }

    /**
     * Atualiza status de ativação do médico.
     * 
     * @param entity Entidade a ser atualizada
     */
    private void updateStatus(Medic entity) {
        if (this.active != null) entity.setActive(this.active);
    }
}
