package br.fai.lds.medlink.domain.dataTransferObject.MedicalRecord.Vaccine;

import br.fai.lds.medlink.domain.Vaccine;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VaccineResponseDto {
    private int id;
    private String name;
    private LocalDate date;

    public static VaccineResponseDto fromEntity(Vaccine entity) {
        return VaccineResponseDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .date(entity.getDate())
                .build();
    }
}