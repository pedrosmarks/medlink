package br.fai.lds.medlink.domain.dataTransferObject.Patient;

import br.fai.lds.medlink.domain.Address;
import br.fai.lds.medlink.domain.Gender;
import br.fai.lds.medlink.domain.Patient;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PatientCreateDto {

    private int id;

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
    private LocalDate birthdate;

    @NotNull(message = "O número de telefone não pode ser nulo")
    private String phoneNumber;

    @NotNull(message = "O endereço não pode ser nulo")
    private Address address;

    @NotNull(message = "O endereço de email não pode estar em branco")
    private String email;

    @NotNull(message = "O plano não pode estar em branco")
    private String plan;

    @NotNull(message = "O crtao do Sus não pode estar em branco")
    private String susCard;


    public Patient toEntity(){
        Patient entity = new Patient();

        entity.setId(this.id);
        entity.setName(this.name);
        entity.setCpf(this.cpf);
        entity.setGender(this.gender);
        entity.setBirthDate(this.birthdate);
        entity.setPhoneNumber(this.phoneNumber);
        entity.setAddress(this.address);
        entity.setEmail(this.email);
        entity.setPlan(this.plan);
        entity.setSusCard(this.susCard);

        return entity;
    }
}
