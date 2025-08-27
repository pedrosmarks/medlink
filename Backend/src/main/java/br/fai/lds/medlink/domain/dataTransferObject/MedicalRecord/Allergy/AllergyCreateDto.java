package br.fai.lds.medlink.domain.dataTransferObject.MedicalRecord.Allergy;

import br.fai.lds.medlink.domain.Allergy;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
public class AllergyCreateDto {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Substance is required")
    private String substance;


    @NotBlank(message = "Name is required")
    private String reaction;

    @NotBlank(message = "Name is required")
    private String severity;

    public Allergy toEntity() {
        return Allergy.builder()
                .substance(this.substance)
                .build();
    }

    public static AllergyCreateDto fromEntity(Allergy entity) {
        if (entity == null) return null;
        return AllergyCreateDto.builder()
                .substance(entity.getSubstance())
                .build();
    }
}

