package br.fai.lds.medlink.domain.dataTransferObject.MedicalRecord.Allergy;

import br.fai.lds.medlink.domain.Allergy;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

/**
 * DTO para criação de novas alergias no prontuário médico.
 * 
 * <p>Contém as informações necessárias para registrar uma nova alergia
 * no histórico médico do paciente.</p>
 * 
 * @author MedLink Team
 * @version 1.0
 * @since 1.0
 */
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

    /**
     * Converte este DTO em uma entidade Allergy.
     * 
     * @return Nova instância de Allergy com os dados deste DTO
     */
    public Allergy toEntity() {
        return Allergy.builder()
                .substance(this.substance)
                .build();
    }

    /**
     * Cria um DTO a partir de uma entidade Allergy.
     * 
     * @param entity Entidade Allergy a ser convertida
     * @return DTO com os dados da alergia ou null se entity for null
     */
    public static AllergyCreateDto fromEntity(Allergy entity) {
        if (entity == null) return null;
        return AllergyCreateDto.builder()
                .substance(entity.getSubstance())
                .build();
    }
}

