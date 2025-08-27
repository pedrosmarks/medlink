package br.fai.lds.medlink.domain.dataTransferObject.MedicalRecord.Medication;

import br.fai.lds.medlink.domain.Medication;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
public class MedicationCreateDto {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Dosage is required")
    private String dosage;

    @NotBlank(message = "Frequency is required")
    private String frequency;

    public Medication toEntity() {
        return Medication.builder()
                .name(this.name)
                .dosage(this.dosage)
                .frequency(this.frequency)
                .build();
    }

    public static MedicationCreateDto fromEntity(Medication entity) {
        if (entity == null) return null;
        return MedicationCreateDto.builder()
                .name(entity.getName())
                .dosage(entity.getDosage())
                .frequency(entity.getFrequency())
                .build();
    }
}
