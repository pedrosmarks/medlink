package br.fai.lds.medlink.domain.dataTransferObject;

import br.fai.lds.medlink.domain.Address;
import br.fai.lds.medlink.domain.Gender;
import br.fai.lds.medlink.domain.Medic;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MedicDto {

    private int id;
    private String name;
    private String cpf;
    private Gender gender;
    private LocalDate birthDade;
    private String phoneNumber;
    private Address address;
    private String crm;
    private String specialty;
    private String email;
    private boolean active = true;

    // Converte de entidade para DTO
    public static MedicDto fromEntity(Medic entity) {
        MedicDto dto = new MedicDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setCpf(entity.getCpf());
        dto.setGender(entity.getGender());
        dto.setBirthDade(entity.getBirthDate());
        dto.setPhoneNumber(entity.getPhoneNumber());
        dto.setAddress(entity.getAddress());
        dto.setCrm(entity.getCrm());
        dto.setSpecialty(entity.getSpecialty());
        dto.setEmail(entity.getEmail());
        dto.setActive(entity.isActive());
        return dto;
    }

    // Converte de DTO para entidade
    public Medic toEntity() {
        Medic entity = new Medic();
        entity.setId(this.id);
        entity.setName(this.name);
        entity.setCpf(this.cpf);
        entity.setGender(this.gender);
        entity.setBirthDate(this.birthDade);
        entity.setPhoneNumber(this.phoneNumber);
        entity.setAddress(this.address);
        entity.setCrm(this.crm);
        entity.setSpecialty(this.specialty);
        entity.setEmail(this.email);
        entity.setActive(this.active);
        return entity;
    }
}
