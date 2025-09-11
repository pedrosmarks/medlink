package br.fai.lds.medlink.domain.dataTransferObject.Patient;

import br.fai.lds.medlink.domain.Address;
import br.fai.lds.medlink.domain.Gender;
import br.fai.lds.medlink.domain.Patient;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatientCreateDto {
    
    @NotNull(message = "O nome não pode ser nulo")
    @Size(min = 2, max = 100, message = "O nome deve ter entre 2 e 100 caracteres")
    private String name;
    
    @NotNull(message = "O CPF não pode ser nulo")
    @Pattern(regexp = "^(\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2})$", message = "O CPF deve estar no formato XXX.XXX.XXX-XX")
    private String cpf;
    
    @NotNull(message = "O gênero não pode ser nulo")
    private Gender gender;
    
    @NotNull(message = "A data de nascimento não pode ser nula")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate birthDate;
    
    @NotNull(message = "O número de telefone não pode ser nulo")
    @Pattern(regexp = "^\\(?\\d{2}\\)?[\\s-]?\\d{4,5}[\\s-]?\\d{4}$", message = "Formato de telefone inválido")
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
    private boolean active = true;

    public Patient toEntity() {
        Patient entity = new Patient();
        entity.setName(this.name);
        entity.setCpf(this.cpf);
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