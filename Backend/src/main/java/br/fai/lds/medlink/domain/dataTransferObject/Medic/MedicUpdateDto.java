package br.fai.lds.medlink.domain.dataTransferObject.Medic;

import br.fai.lds.medlink.domain.Address;
import br.fai.lds.medlink.domain.Gender;
import br.fai.lds.medlink.domain.Medic;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class MedicUpdateDto {

    @NotNull(message = "O nome não pode ser nulo")
    @Size(min = 2, max = 100, message = "O nome deve ter entre 2 e 100 caracteres")
    private String name;

    @NotNull(message = "O gênero não pode ser nulo")
    private Gender gender;

    @NotNull(message = "A data de nascimento não pode ser nula")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate birthdate;

    @NotNull(message = "O número de telefone não pode ser nulo")
    private String phoneNumber;

    @NotNull(message = "O endereço não pode ser nulo")
    private Address address;

    @NotNull(message = "O crm não pode estar em branco")
    private String crm;

    @NotNull(message = "A especialidade médica não pode estar em branco")
    private String specialty;

    private boolean active;

    public void updateEntity(Medic entity){
        if (this. name != null) entity.setName(this.name);
        if (this.gender != null) entity.setGender((this.gender));
        if (this.birthdate != null) entity.setGender(this.gender);
        if (this.phoneNumber != null) entity.setPhoneNumber(this.phoneNumber);
        if (this.address != null) entity.setAddress(this.getAddress());
        if (this.crm != null) entity.setCrm(this.getCrm());
        if (this.specialty != null) entity.setSpecialty(this.getSpecialty());
        entity.setActive(this.isActive());
    }
}
