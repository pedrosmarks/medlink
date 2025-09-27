package br.fai.lds.medlink.domain.dataTransferObject.MedicalRecord.Consultation;

import br.fai.lds.medlink.domain.Consultation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConsultationResponseDto {
    private LocalDate date;
    private String reason;
    private String notes;
    private boolean softDeleted;

    public static ConsultationResponseDto fromEntity(Consultation entity) {
        return ConsultationResponseDto.builder()
                .date(entity.getDate())
                .reason(entity.getReason())
                .notes(entity.getNotes())
                .softDeleted(entity.isSoftDeleted())
                .build();
    }
}