package br.fai.lds.medlink.domain.dataTransferObject.Medic;

import br.fai.lds.medlink.domain.Address;
import br.fai.lds.medlink.domain.Gender;
import br.fai.lds.medlink.domain.Medic;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class MedicUpdateDto {

    @Size(min = 2, max = 100, message = "O nome deve ter entre 2 e 100 caracteres")
    private String name;

    @Pattern(regexp = "^(\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2})$", message = "O CPF deve estar no formato XXX.XXX.XXX-XX")
    private String cpf;

    private Gender gender;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate birthDate;

    @Pattern(regexp = "^\\(?\\d{2}\\)?[\\s-]?\\d{4,5}[\\s-]?\\d{4}$", message = "Formato de telefone inválido")
    private String phoneNumber;

    private Address address;

    @Pattern(regexp = "^\\d{4,6}/[A-Z]{2}$", message = "CRM deve estar no formato NNNN/UF ou NNNNNN/UF")
    private String crm;

    @Size(min = 2, max = 100, message = "A especialidade deve ter entre 2 e 100 caracteres")
    private String specialty;

    @Email(message = "O e-mail deve ser válido")
    private String email;

    private Boolean active;

    public void updateEntity(Medic entity) {
        updateBasicInfo(entity);
        updateContactInfo(entity);
        updateProfessionalInfo(entity);
        updateStatus(entity);
    }

    private void updateBasicInfo(Medic entity) {
        if (this.name != null) entity.setName(this.name);
        if (this.cpf != null) entity.setCpf(this.cpf);
        if (this.gender != null) entity.setGender(this.gender);
        if (this.birthDate != null) entity.setBirthDate(this.birthDate);
    }

    private void updateContactInfo(Medic entity) {
        if (this.phoneNumber != null) entity.setPhoneNumber(this.phoneNumber);
        if (this.address != null) entity.setAddress(this.address);
        if (this.email != null) entity.setEmail(this.email);
    }

    private void updateProfessionalInfo(Medic entity) {
        if (this.crm != null) entity.setCrm(this.crm);
        if (this.specialty != null) entity.setSpecialty(this.specialty);
    }

    private void updateStatus(Medic entity) {
        if (this.active != null) entity.setActive(this.active);
    }
}
