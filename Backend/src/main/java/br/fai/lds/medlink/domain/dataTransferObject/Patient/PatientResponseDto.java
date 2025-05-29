package br.fai.lds.medlink.domain.dataTransferObject.Patient;

import br.fai.lds.medlink.domain.Address;
import br.fai.lds.medlink.domain.Gender;
import br.fai.lds.medlink.domain.Patient;
import lombok.Data;
import java.time.LocalDate;

@Data
public class PatientResponseDto {

    private String name;
    private Gender gender;
    private LocalDate birthdate;
    private String phoneNumber;
    private Address address;
    private String email;
    private String plan;
    private String susCard;

    public static PatientResponseDto fromEntity(Patient entity) {
        PatientResponseDto dto = new PatientResponseDto();
        dto.setName(entity.getName());
        dto.setGender(entity.getGender());
        dto.setPhoneNumber(entity.getPhoneNumber());
        dto.setAddress(entity.getAddress());
        dto.setEmail(entity.getEmail());
        dto.setPlan(entity.getPlan());
        dto.setSusCard(entity.getSusCard());
        return dto;
    }
}
