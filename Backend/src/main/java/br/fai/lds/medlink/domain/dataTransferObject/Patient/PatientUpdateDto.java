package br.fai.lds.medlink.domain.dataTransferObject.Patient;

import br.fai.lds.medlink.domain.Address;
import br.fai.lds.medlink.domain.Gender;
import br.fai.lds.medlink.domain.Medic;
import br.fai.lds.medlink.domain.Patient;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PatientUpdateDto {

    private String name;
    private String cpf;
    private Gender gender;
    private LocalDate birthdate;
    private String phoneNumber;
    private Address address;
    private String email;
    private String plan;
    private String susCard;
    private boolean active;


    public void updateEntity(Patient entity){
        if (this. name != null) entity.setName(this.name);
        if (this.gender != null) entity.setGender((this.gender));
        if (this.birthdate != null) entity.setGender(this.gender);
        if (this.phoneNumber != null) entity.setPhoneNumber(this.phoneNumber);
        if (this.address != null) entity.setAddress(this.getAddress());
        if (this.email != null) entity.setEmail(this.getEmail());
        if (this.plan != null) entity.setPlan(this.getPlan());
        if (this.susCard != null) entity.setSusCard(this.getSusCard());
        entity.setActive(this.isActive());
    }
}
