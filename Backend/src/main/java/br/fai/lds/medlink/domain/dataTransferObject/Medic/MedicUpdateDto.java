package br.fai.lds.medlink.domain.dataTransferObject.Medic;

import br.fai.lds.medlink.domain.Address;
import br.fai.lds.medlink.domain.Gender;
import br.fai.lds.medlink.domain.Medic;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class MedicUpdateDto {

    @Size(min = 2, max = 100, message = "O nome deve ter entre 2 e 100 caracteres")
    private String name;

    private Gender gender;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate birthDate;

    private String phoneNumber;

    private Address address;

    private String crm;

    private String specialty;

    private Boolean active;

    public void updateEntity(Medic entity) {
        if (this.name != null) entity.setName(this.name);
        if (this.gender != null) entity.setGender(this.gender);
        if (this.birthDate != null) entity.setBirthDate(this.birthDate);
        if (this.phoneNumber != null) entity.setPhoneNumber(this.phoneNumber);
        if (this.address != null) entity.setAddress(this.address);
        if (this.crm != null) entity.setCrm(this.crm);
        if (this.specialty != null) entity.setSpecialty(this.specialty);
        if (this.active != null) entity.setActive(this.active);
    }
}
