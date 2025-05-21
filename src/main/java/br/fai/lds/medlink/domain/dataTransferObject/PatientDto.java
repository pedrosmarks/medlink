package br.fai.lds.medlink.domain.dataTransferObject;

import br.fai.lds.medlink.domain.Address;
import br.fai.lds.medlink.domain.Patient;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PatientDto {

    private int id;
    private String name;
    private String cpf;
    private Gender gender;
    private LocalDate dataNascimento;
    private String phoneNumber;
    private Address address;

    private String email;
    private String plan;
    private String susCard;
    private boolean active = true;

    /**
     * Converte uma entidade Patient para um DTO.
     */
    public static PatientDto fromEntity(Patient entity) {
        PatientDto dto = new PatientDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setCpf(entity.getCpf());
        dto.setGender(entity.getGender());
        dto.setDataNascimento(entity.getDataNascimento());
        dto.setPhoneNumber(entity.getPhoneNumber());
        dto.setAddress(entity.getAddress());
        dto.setEmail(entity.getEmail());
        dto.setPlan(entity.getPlan());
        dto.setSusCard(entity.getSusCard());
        dto.setActive(entity.isActive());
        return dto;
    }

    /**
     * Converte este DTO em uma entidade Patient.
     */
    public Patient toEntity() {
        Patient entity = new Patient();
        entity.setId(this.id);
        entity.setName(this.name);
        entity.setCpf(this.cpf);
        entity.setGender(this.gender);
        entity.setDataNascimento(this.dataNascimento);
        entity.setPhoneNumber(this.phoneNumber);
        entity.setAddress(this.address);
        entity.setEmail(this.email);
        entity.setPlan(this.plan);
        entity.setSusCard(this.susCard);
        entity.setActive(this.active);
        return entity;
    }
}
