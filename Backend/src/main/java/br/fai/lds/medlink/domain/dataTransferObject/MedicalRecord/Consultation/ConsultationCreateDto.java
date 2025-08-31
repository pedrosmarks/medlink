package br.fai.lds.medlink.domain.dataTransferObject.MedicalRecord.Consultation;

import br.fai.lds.medlink.domain.Consultation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Data
@Builder
public class ConsultationCreateDto {

    @NotNull(message = "Consultation date is required")
    private LocalDate date;

    @NotBlank(message = "Reason is required")
    private String reason;

    private String notes;

    public Consultation toEntity() {
        return Consultation.builder()
                .date(this.date)
                .reason(this.reason)
                .notes(this.notes)
                .build();
    }

    public static ConsultationCreateDto fromEntity(Consultation entity) {
        if (entity == null) return null;
        return ConsultationCreateDto.builder()
                .date(entity.getDate())
                .reason(entity.getReason())
                .notes(entity.getNotes())
                .build();
    }
}
