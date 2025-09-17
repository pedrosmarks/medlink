package br.fai.lds.medlink.domain.dataTransferObject.MedicalRecord.Surgery;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO para atualização de dados de cirurgias existentes.
 * 
 * <p>Permite atualização parcial dos dados de cirurgia, onde apenas
 * os campos não nulos serão atualizados na entidade existente.</p>
 * 
 * @author MedLink Team
 * @version 1.0
 * @since 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SurgeryUpdateDto {
    
    /**
     * Novo nome da cirurgia (opcional).
     */
    @Size(min = 2, max = 100, message = "Nome da cirurgia deve ter entre 2 e 100 caracteres")
    private String name;
    
    /**
     * Nova data da cirurgia (opcional).
     */
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate date;
    
    /**
     * Novo local onde a cirurgia foi realizada (opcional).
     */
    @Size(min = 2, max = 100, message = "Local deve ter entre 2 e 100 caracteres")
    private String location;
    
    /**
     * Novas observações sobre a cirurgia (opcional).
     */
    @Size(max = 500, message = "Observações devem ter no máximo 500 caracteres")
    private String notes;
}