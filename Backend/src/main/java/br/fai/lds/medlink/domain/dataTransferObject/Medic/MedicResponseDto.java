package br.fai.lds.medlink.domain.dataTransferObject.Medic;

import br.fai.lds.medlink.domain.Address;
import br.fai.lds.medlink.domain.Gender;
import br.fai.lds.medlink.domain.Medic;
import lombok.Data;

import java.time.LocalDate;

@Data
public class MedicResponseDto {

    private int id;
    private String name;
    private String cpf;
    private Gender gender;
    private LocalDate birthDate;
    private String phoneNumber;
    private Address address;
    private String crm;
    private String specialty;
    private String email;
    private boolean active;

    public static MedicResponseDto fromEntity(Medic entity) {
        MedicResponseDto dto = new MedicResponseDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setCpf(entity.getCpf());
        dto.setGender(entity.getGender());
        dto.setBirthDate(entity.getBirthDate());
        dto.setPhoneNumber(entity.getPhoneNumber());
        dto.setAddress(entity.getAddress());
        dto.setCrm(entity.getCrm());
        dto.setSpecialty(entity.getSpecialty());
        dto.setEmail(entity.getEmail());
        dto.setActive(entity.isActive());
        return dto;
    }
}
