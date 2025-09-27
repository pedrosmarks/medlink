package br.fai.lds.medlink.domain.dataTransferObject.MedicalRecord.Diagnosis;

import br.fai.lds.medlink.domain.Diagnosis;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiagnosisResponseDto {
    private int id;
    private String description;
    private LocalDate date;
    private boolean softDeleted;

    public static DiagnosisResponseDto fromEntity(Diagnosis entity) {
        return DiagnosisResponseDto.builder()
                .id(entity.getId())
                .description(entity.getDescription())
                .date(entity.getDate())
                .softDeleted(entity.isSoftDeleted())
                .build();
    }
}