package br.fai.lds.medlink.domain.dataTransferObject.MedicalRecord.Consultation;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO para atualização de dados de consultas existentes.
 * 
 * <p>Permite atualização parcial dos dados de consulta, onde apenas
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
public class ConsultationUpdateDto {
    
    /**
     * Nova data da consulta (opcional).
     */
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate date;
    
    /**
     * Novo motivo da consulta (opcional).
     */
    @Size(min = 5, max = 200, message = "Motivo da consulta deve ter entre 5 e 200 caracteres")
    private String reason;
    
    /**
     * Novas observações da consulta (opcional).
     */
    @Size(max = 500, message = "Observações devem ter no máximo 500 caracteres")
    private String notes;
}