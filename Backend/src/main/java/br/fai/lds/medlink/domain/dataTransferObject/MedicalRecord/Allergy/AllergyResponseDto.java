package br.fai.lds.medlink.domain.dataTransferObject.MedicalRecord.Allergy;

import br.fai.lds.medlink.domain.Allergy;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AllergyResponseDto {
    private int id;
    private String name;
    private String substance;
    private String reaction;
    private String severity;

    public static AllergyResponseDto fromEntity(Allergy entity) {
        return AllergyResponseDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .substance(entity.getSubstance())
                .reaction(entity.getReaction())
                .severity(entity.getSeverity())
                .build();
    }
}