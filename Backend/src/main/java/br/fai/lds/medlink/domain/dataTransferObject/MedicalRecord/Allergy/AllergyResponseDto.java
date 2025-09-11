package br.fai.lds.medlink.domain.dataTransferObject.MedicalRecord.Allergy;

import br.fai.lds.medlink.domain.Allergy;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para resposta com dados de alergia.
 * 
 * <p>Utilizado para retornar informações de alergias do paciente
 * em consultas e relatórios médicos.</p>
 * 
 * @author MedLink Team
 * @version 1.0
 * @since 1.0
 */
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

    /**
     * Cria um DTO de resposta a partir de uma entidade Allergy.
     * 
     * @param entity Entidade Allergy a ser convertida
     * @return DTO com os dados da alergia formatados para resposta
     */
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