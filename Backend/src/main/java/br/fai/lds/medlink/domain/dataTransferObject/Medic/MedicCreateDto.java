package br.fai.lds.medlink.domain.dataTransferObject.Medic;

import br.fai.lds.medlink.domain.Address;
import br.fai.lds.medlink.domain.Gender;
import br.fai.lds.medlink.domain.Medic;

import java.time.LocalDate;

public class MedicCreateDto {

    private String name;
    private String cpf;
    private Gender gender;
    private LocalDate birthdate;
    private String phoneNumber;
    private Address adress;
    private String crm;
    private String specialty;
    private String email;
    private String password;


    public Medic toEntity(){
        Medic entity = new Medic();

        entity.setName(this.name);
        entity.setCpf(this.cpf);
        entity.setGender(this.gender);
        entity.setBirthDate(this.birthdate);
        entity.setPhoneNumber(this.phoneNumber);
        entity.setAddress(this.adress);
        entity.setCrm(this.crm);
        entity.setSpecialty(this.specialty);
        entity.setEmail(this.email);
        entity.setPassword(this.password);

        return entity;
    }
}
