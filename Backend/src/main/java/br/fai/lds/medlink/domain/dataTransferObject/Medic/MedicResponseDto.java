package br.fai.lds.medlink.domain.dataTransferObject.Medic;

import br.fai.lds.medlink.domain.Medic;
import lombok.Data;

@Data
public class MedicResponseDto {

    private String name;
    private String crm;
    private String specialty;

    public static MedicResponseDto fromEntity(Medic entity) {
        MedicResponseDto dto = new MedicResponseDto();
        dto.setName(entity.getName());
        dto.setSpecialty(entity.getSpecialty());
        return dto;
    }
}
