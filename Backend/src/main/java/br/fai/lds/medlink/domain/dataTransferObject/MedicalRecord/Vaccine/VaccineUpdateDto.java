package br.fai.lds.medlink.domain.dataTransferObject.MedicalRecord.Vaccine;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO para atualização de dados de vacinas existentes.
 * 
 * <p>Permite atualização parcial dos dados de vacina, onde apenas
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
public class VaccineUpdateDto {
    
    /**
     * Novo nome da vacina (opcional).
     */
    @Size(min = 2, max = 100, message = "Nome da vacina deve ter entre 2 e 100 caracteres")
    private String name;
    
    /**
     * Nova data de aplicação da vacina (opcional).
     */
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate date;
}