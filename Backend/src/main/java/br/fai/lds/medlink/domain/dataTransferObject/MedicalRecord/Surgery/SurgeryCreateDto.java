package br.fai.lds.medlink.domain.dataTransferObject.MedicalRecord.Surgery;

import br.fai.lds.medlink.domain.Surgery;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Data
@Builder
public class SurgeryCreateDto {

    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "Date is required")
    private String date;

    private String location;

    private String notes;

    public Surgery toEntity() {
        return Surgery.builder()
                .name(this.name)
                .date(this.date)
                .location(this.location)
                .notes(this.notes)
                .build();
    }

    public static SurgeryCreateDto fromEntity(Surgery entity) {
        if (entity == null) return null;
        return SurgeryCreateDto.builder()
                .name(entity.getName())
                .date(entity.getDate())
                .location(entity.getLocation())
                .notes(entity.getNotes())
                .build();
    }
}
