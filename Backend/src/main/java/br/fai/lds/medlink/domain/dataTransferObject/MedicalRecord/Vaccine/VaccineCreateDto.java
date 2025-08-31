package br.fai.lds.medlink.domain.dataTransferObject.MedicalRecord.Vaccine;

import br.fai.lds.medlink.domain.Vaccine;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Data
@Builder
public class VaccineCreateDto {

    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "Date is required")
    private LocalDate date;

    public Vaccine toEntity() {
        return Vaccine.builder()
                .name(this.name)
                .date(this.date)
                .build();
    }

    public static VaccineCreateDto fromEntity(Vaccine entity) {
        if (entity == null) return null;
        return VaccineCreateDto.builder()
                .name(entity.getName())
                .date(entity.getDate())
                .build();
    }
}

