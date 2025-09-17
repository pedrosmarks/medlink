package br.fai.lds.medlink.domain.dataTransferObject.MedicalRecord.Diagnosis;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO para atualização de dados de diagnósticos existentes.
 * 
 * <p>Permite atualização parcial dos dados de diagnóstico, onde apenas
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
public class DiagnosisUpdateDto {
    
    /**
     * Nova descrição do diagnóstico (opcional).
     */
    @Size(min = 5, max = 500, message = "Descrição do diagnóstico deve ter entre 5 e 500 caracteres")
    private String description;
    
    /**
     * Nova data do diagnóstico (opcional).
     * Não pode ser uma data futura.
     */
    @JsonFormat(pattern = "dd/MM/yyyy")
    @PastOrPresent(message = "Data do diagnóstico não pode ser futura")
    private LocalDate date;
}