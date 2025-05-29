package br.fai.lds.medlink.domain.dataTransferObject.Patient;

import br.fai.lds.medlink.domain.Address;
import br.fai.lds.medlink.domain.Gender;
import br.fai.lds.medlink.domain.Patient;

import java.time.LocalDate;

public class PatientCreateDto {

    private String name;
    private String cpf;
    private Gender gender;
    private LocalDate birthdate;
    private String phoneNumber;
    private Address address;
    private String email;
    private String plan;
    private String susCard;


    public Patient toEntity(){
        Patient entity = new Patient();

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
