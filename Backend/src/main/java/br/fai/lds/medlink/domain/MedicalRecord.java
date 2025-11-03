package br.fai.lds.medlink.domain;

import br.fai.lds.medlink.domain.enuns.BloodType;
import br.fai.lds.medlink.domain.enuns.OrganDonorStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Entidade que representa o prontuário médico completo de um paciente.
 * 
 * <p>Centraliza todas as informações médicas do paciente, incluindo
 * histórico de consultas, medicações, alergias, vacinas e cirurgias.</p>
 * 
 * @author MedLink Team
 * @version 1.0
 * @since 1.0
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class MedicalRecord {

    /** ID único do prontuário médico. */
    private int id;

    /** Tipo sanguíneo do paciente. */
    @NotNull(message = "O tipo sanguíneo é obrigatório")
    private BloodType bloodType;

    /** Status de doador de órgãos do paciente. */
    @NotNull(message = "A informação de doador de órgãos é obrigatória")
    private OrganDonorStatus organDonor;

    /** Diagnóstico médico principal do paciente. */
    @NotNull(message = "O diagnóstico é obrigatório")
    private String diagnosis;

    /** Histórico familiar de doenças e condições médicas. */
    @NotNull(message = "O histórico familiar é obrigatório")
    private String familyHistory;

    /** Indica se o prontuário médico está ativo. */
    private boolean medicalRecordActive = true;

    /** ID do paciente proprietário do prontuário. */
    private Integer patientId;

    /** Lista de alergias conhecidas do paciente. */
    @Builder.Default
    private List<Allergy> allergies = new ArrayList<>();

    /** Histórico de vacinação do paciente. */
    @Builder.Default
    private List<Vaccine> vaccines = new ArrayList<>();

    /** Histórico de cirurgias realizadas. */
    @Builder.Default
    private List<Surgery> surgeries = new ArrayList<>();

    /** Lista de medicações prescritas ou em uso. */
    @Builder.Default
    private List<Medication> medications = new ArrayList<>();

    /** Histórico de consultas médicas realizadas. */
    @Builder.Default
    private List<Consultation> consultations = new ArrayList<>();
}
