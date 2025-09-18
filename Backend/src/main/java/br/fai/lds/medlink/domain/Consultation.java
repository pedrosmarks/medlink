package br.fai.lds.medlink.domain;

import lombok.*;
import java.time.LocalDate;

/**
 * Entidade que representa uma consulta médica no prontuário.
 * 
 * <p>Registra informações sobre consultas realizadas, incluindo
 * data, motivo e observações médicas.</p>
 * 
 * @author MedLink Team
 * @version 1.0
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Consultation {
    /** Data da consulta médica. */
    private LocalDate date;
    
    /** Motivo ou razão da consulta. */
    private String reason;
    
    /** Observações e anotações médicas da consulta. */
    private String notes;
}
