package br.fai.lds.medlink.domain.dataTransferObject.Patient;

import br.fai.lds.medlink.domain.Address;
import br.fai.lds.medlink.domain.Gender;
import br.fai.lds.medlink.domain.Patient;
import lombok.Data;
import java.time.LocalDate;

@Data
public class PatientResponseDto {
    private int id;
    private String name;
    private String cpf;
    private Gender gender;
    private LocalDate birthdate;
    private String phoneNumber;
    private String avatar;
    private String bloodType;
    private String observations;
    private Address address;
    private String email;
    private String plan;
    private String susCard;
    private Integer medicId;
    private boolean active;

    public static PatientResponseDto fromEntity(Patient entity) {
        PatientResponseDto dto = new PatientResponseDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setCpf(entity.getCpf());
        dto.setGender(entity.getGender());
        dto.setBirthdate(entity.getBirthDate());
        dto.setPhoneNumber(entity.getPhoneNumber());
        dto.setAvatar(entity.getAvatar());
        dto.setBloodType(entity.getBloodType());
        dto.setObservations(entity.getObservations());
        dto.setAddress(entity.getAddress());
        dto.setEmail(entity.getEmail());
        dto.setPlan(entity.getPlan());
        dto.setSusCard(entity.getSusCard());
        dto.setMedicId(entity.getMedicId());
        dto.setActive(entity.isActive());
        return dto;
    }
}