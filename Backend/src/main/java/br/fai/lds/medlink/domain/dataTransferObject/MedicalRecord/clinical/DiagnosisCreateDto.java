package br.fai.lds.medlink.domain.dataTransferObject.MedicalRecord.clinical;

import br.fai.lds.medlink.domain.Diagnosis;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Data
@Builder
public class DiagnosisCreateDto {

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Date is required")
    private String date;

    public Diagnosis toEntity() {
        return Diagnosis.builder()
                .description(this.description)
                .date(this.date)
                .build();
    }

    public static DiagnosisCreateDto fromEntity(Diagnosis entity) {
        if (entity == null) return null;
        return DiagnosisCreateDto.builder()
                .description(entity.getDescription())
                .date(entity.getDate())
                .build();
    }
}
