package br.fai.lds.medlink.domain;

import lombok.*;
import java.time.LocalDate;
import jakarta.persistence.*;

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
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Consultation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    /** Identificador único da consulta. */
    private int id;

    /** Data da consulta médica. */
    private LocalDate date;
    
    /** Motivo ou razão da consulta. */
    private String reason;
    
    /** Observações e anotações médicas da consulta. */
    private String notes;

    private boolean softDeleted;
    public boolean isSoftDeleted() { return softDeleted; }
    public void setSoftDeleted(boolean softDeleted) { this.softDeleted = softDeleted; }
}
