package br.fai.lds.medlink.domain.dataTransferObject.MedicalRecord.Consultation;

import br.fai.lds.medlink.domain.Consultation;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;

import java.time.LocalDate;

/**
 * DTO para criação de novas consultas no prontuário médico.
 * 
 * <p>Contém as informações necessárias para registrar uma nova consulta
 * no histórico médico do paciente.</p>
 * 
 * @author MedLink Team
 * @version 1.0
 * @since 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConsultationCreateDto {

    /**
     * Data da consulta médica.
     * Não pode ser uma data futura.
     */
    @NotNull(message = "Consultation date is required")
    @JsonFormat(pattern = "dd/MM/yyyy")
    @PastOrPresent(message = "Data da consulta não pode ser futura")
    private LocalDate date;

    /**
     * Motivo ou razão da consulta médica.
     */
    @NotBlank(message = "Reason is required")
    private String reason;

    /**
     * Observações adicionais sobre a consulta.
     */
    private String notes;

    /**
     * Converte este DTO em uma entidade Consultation.
     * 
     * @return Nova instância de Consultation com os dados deste DTO
     */
    public Consultation toEntity() {
        return Consultation.builder()
                .date(this.date)
                .reason(this.reason)
                .notes(this.notes)
                .build();
    }

    /**
     * Cria um DTO a partir de uma entidade Consultation.
     * 
     * @param entity Entidade Consultation a ser convertida
     * @return DTO com os dados da consulta ou null se entity for null
     */
    public static ConsultationCreateDto fromEntity(Consultation entity) {
        if (entity == null) return null;
        return ConsultationCreateDto.builder()
                .date(entity.getDate())
                .reason(entity.getReason())
                .notes(entity.getNotes())
                .build();
    }
}
