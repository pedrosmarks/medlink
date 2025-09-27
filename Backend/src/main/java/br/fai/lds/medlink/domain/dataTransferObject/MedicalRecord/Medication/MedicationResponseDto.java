package br.fai.lds.medlink.domain.dataTransferObject.MedicalRecord.Medication;

import br.fai.lds.medlink.domain.Medication;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedicationResponseDto {
    private String name;
    private String dosage;
    private String frequency;
    private boolean softDeleted;

    public static MedicationResponseDto fromEntity(Medication entity) {
        return MedicationResponseDto.builder()
                .name(entity.getName())
                .dosage(entity.getDosage())
                .frequency(entity.getFrequency())
                .softDeleted(entity.isSoftDeleted())
                .build();
    }
}