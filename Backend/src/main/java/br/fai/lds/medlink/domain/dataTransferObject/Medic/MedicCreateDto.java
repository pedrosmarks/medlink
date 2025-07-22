package br.fai.lds.medlink.domain.dataTransferObject.Medic;

import br.fai.lds.medlink.domain.Address;
import br.fai.lds.medlink.domain.Gender;
import br.fai.lds.medlink.domain.Medic;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

//DTO para criação de médico.


@Data
public class MedicCreateDto {

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
    private String phoneNumber;

    @NotNull(message = "O endereço não pode ser nulo")
    private Address address;

    @NotNull(message = "O CRM não pode estar em branco")
    private String crm;

    @NotNull(message = "A especialidade médica não pode estar em branco")
    private String specialty;

    @NotNull(message = "O endereço de e-mail não pode estar em branco")
    @Email(message = "O e-mail deve ser válido")
    private String email;

    @NotNull(message = "A senha não pode estar em branco")
    @Size(min = 6, max = 100, message = "A senha deve ter entre 6 e 100 caracteres")
    private String password;

    //Converte este DTO em uma entidade Medic.

    public Medic toEntity() {
        Medic entity = new Medic();
        entity.setName(this.name);
        entity.setCpf(this.cpf);
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
