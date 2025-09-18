package br.fai.lds.medlink.domain.dataTransferObject.MedicalRecord;

import br.fai.lds.medlink.domain.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

/**
 * DTO para atualização de dados do prontuário médico.
 * 
 * <p>Permite atualização parcial dos dados do prontuário médico, onde apenas
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
public class MedicalRecordUpdateDto {
    /**
     * Novo tipo sanguíneo do paciente (opcional).
     */
    private BloodType bloodType;
    
    /**
     * Novo status de doador de órgãos (opcional).
     */
    private OrganDonorStatus organDonor;
    
    /**
     * Novo diagnóstico médico (opcional).
     */
    @Size(min = 5, max = 500, message = "Diagnóstico deve ter entre 5 e 500 caracteres")
    private String diagnosis;
    
    /**
     * Novo histórico familiar (opcional).
     */
    @Size(min = 5, max = 500, message = "Histórico familiar deve ter entre 5 e 500 caracteres")
    private String familyHistory;
    
    /**
     * Nova lista de alergias (opcional).
     */
    private List<Allergy> allergies;
    
    /**
     * Nova lista de vacinas (opcional).
     */
    private List<Vaccine> vaccines;
    
    /**
     * Nova lista de cirurgias (opcional).
     */
    private List<Surgery> surgeries;
    
    /**
     * Nova lista de medicações (opcional).
     */
    private List<Medication> medications;
    
    /**
     * Nova lista de consultas (opcional).
     */
    private List<Consultation> consultations;
    
    /**
     * Novo status de ativo do prontuário (opcional).
     */
    private Boolean medicalRecordActive;

    /**
     * Converte este DTO em uma entidade MedicalRecord.
     * 
     * @return Nova instância de MedicalRecord com os dados deste DTO
     */
    public MedicalRecord toEntity() {
        return MedicalRecord.builder()
                .bloodType(this.bloodType)
                .organDonor(this.organDonor)
                .diagnosis(this.diagnosis)
                .familyHistory(this.familyHistory)
                .allergies(this.allergies != null ? this.allergies : List.of())
                .vaccines(this.vaccines != null ? this.vaccines : List.of())
                .surgeries(this.surgeries != null ? this.surgeries : List.of())
                .medications(this.medications != null ? this.medications : List.of())
                .consultations(this.consultations != null ? this.consultations : List.of())
                .medicalRecordActive(this.medicalRecordActive != null ? this.medicalRecordActive : true)
                .build();
    }

}
