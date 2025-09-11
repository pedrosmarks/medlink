package br.fai.lds.medlink.domain.dataTransferObject.MedicalRecord.Surgery;

import br.fai.lds.medlink.domain.Surgery;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SurgeryResponseDto {
    private int id;
    private String name;
    private LocalDate date;
    private String location;
    private String notes;

    public static SurgeryResponseDto fromEntity(Surgery entity) {
        return SurgeryResponseDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .date(entity.getDate())
                .location(entity.getLocation())
                .notes(entity.getNotes())
                .build();
    }
}