package br.fai.lds.medlink.domain.dataTransferObject.Medic;

import br.fai.lds.medlink.domain.Address;
import br.fai.lds.medlink.domain.Gender;
import br.fai.lds.medlink.domain.Medic;
import lombok.Data;

import java.time.LocalDate;

@Data
public class MedicUpdateDto {

    private String name;
    private Gender gender;
    private LocalDate birthdate;
    private String phoneNumber;
    private Address address;
    private String crm;
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
