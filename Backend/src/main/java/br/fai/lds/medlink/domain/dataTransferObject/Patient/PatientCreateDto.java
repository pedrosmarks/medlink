package br.fai.lds.medlink.domain.dataTransferObject.Patient;

import br.fai.lds.medlink.domain.Address;
import br.fai.lds.medlink.domain.enuns.Gender;
import br.fai.lds.medlink.domain.Patient;
import br.fai.lds.medlink.util.CpfUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO para criação de novos pacientes.
 * 
 * <p>Contém todas as validações necessárias para garantir a integridade dos dados
 * durante o processo de cadastro de um novo paciente no sistema.</p>
 * 
 * @author MedLink Team
 * @version 1.0
 * @since 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatientCreateDto {
    
    @NotNull(message = "O nome não pode ser nulo")
    @Size(min = 2, max = 100, message = "O nome deve ter entre 2 e 100 caracteres")
    private String name;
    
    /**
     * CPF (Cadastro de Pessoa Física) do paciente.
     * <p>
     * Pode ser fornecido no formato XXX.XXX.XXX-XX ou apenas números XXXXXXXXXXX.
     * A validação aceita ambos os formatos e será normalizado internamente.
     * </p>
     */
    @NotNull(message = "O CPF não pode ser nulo")
    @Pattern(regexp = "^(\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}|\\d{11})$",
             message = "O CPF deve estar no formato XXX.XXX.XXX-XX ou conter apenas 11 dígitos")
    private String cpf;
    
    @NotNull(message = "O gênero não pode ser nulo")
    private Gender gender;
    
    @NotNull(message = "A data de nascimento não pode ser nula")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Past(message = "Data de nascimento deve ser no passado")
    private LocalDate birthDate;
    
    @NotNull(message = "O número de telefone não pode ser nulo")
    @Pattern(regexp = "^\\(?[1-9]{2}\\)?[\\s-]?9?[0-9]{4}[\\s-]?[0-9]{4}$", message = "Telefone deve estar no formato (XX) 9XXXX-XXXX ou (XX) XXXX-XXXX")
    private String phoneNumber;
    
    @NotNull(message = "O endereço não pode ser nulo")
    private Address address;
    
    @NotNull(message = "O endereço de e-mail não pode ser nulo")
    @Email(message = "O e-mail deve ser válido")
    private String email;
    
    @NotNull(message = "A senha não pode ser nula")
    @Size(min = 6, max = 100, message = "A senha deve ter entre 6 e 100 caracteres")
    private String password;
    
    private String avatar;
    private String bloodType;
    private String observations;
    private String plan;
    private String susCard;
    @Builder.Default
    private boolean active = true;

    /**
     * Converte este DTO em uma entidade Patient.
     * <p>
     * A validação do CPF é feita durante a conversão para garantir integridade dos dados.
     * </p>
     *
     * @return Nova instância de Patient com os dados deste DTO
     * @throws IllegalArgumentException se o CPF não tiver o comprimento correto após normalização
     */
    public Patient toEntity() {
        // Valida o CPF antes de criar a entidade
        if (!CpfUtil.isValidLength(this.cpf)) {
            throw new IllegalArgumentException("CPF deve ter exatamente 11 dígitos");
        }

        Patient entity = new Patient();
        entity.setName(this.name);
        entity.setCpf(this.cpf); // Mantém o formato original, será normalizado no DAO
        entity.setGender(this.gender);
        entity.setBirthDate(this.birthDate);
        entity.setPhoneNumber(this.phoneNumber);
        entity.setAddress(this.address);
        entity.setEmail(this.email);
        entity.setPassword(this.password);
        entity.setAvatar(this.avatar);
        entity.setBloodType(this.bloodType);
        entity.setObservations(this.observations);
        entity.setPlan(this.plan);
        entity.setSusCard(this.susCard);
        entity.setActive(this.active);
        return entity;
    }
}