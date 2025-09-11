package br.fai.lds.medlink.domain.dataTransferObject.MedicalRecord.Vaccine;

import br.fai.lds.medlink.domain.Vaccine;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO para resposta com dados de vacina.
 * 
 * <p>Utilizado para retornar informações de vacinas do paciente
 * em consultas e relatórios de imunização.</p>
 * 
 * @author MedLink Team
 * @version 1.0
 * @since 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VaccineResponseDto {
    private int id;
    private String name;
    private LocalDate date;

    /**
     * Cria um DTO de resposta a partir de uma entidade Vaccine.
     * 
     * @param entity Entidade Vaccine a ser convertida
     * @return DTO com os dados da vacina formatados para resposta
     */
    public static VaccineResponseDto fromEntity(Vaccine entity) {
        return VaccineResponseDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .date(entity.getDate())
                .build();
    }
}