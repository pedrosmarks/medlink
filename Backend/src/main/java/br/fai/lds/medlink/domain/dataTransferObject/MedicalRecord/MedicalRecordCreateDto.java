package br.fai.lds.medlink.domain.dataTransferObject.MedicalRecord;

import br.fai.lds.medlink.domain.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

/**
 * DTO para criação de novos prontuários médicos.
 * 
 * <p>Contém todas as informações necessárias para criar um prontuário
 * médico completo, incluindo dados clínicos e histórico do paciente.</p>
 * 
 * @author MedLink Team
 * @version 1.0
 * @since 1.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicalRecordCreateDto {

    @NotNull(message = "O tipo sanguíneo é obrigatório")
    private BloodType bloodType;

    @NotNull(message = "O status de doador de órgãos é obrigatório")
    private OrganDonorStatus organDonor;

    @NotBlank(message = "O diagnóstico é obrigatório")
    @Size(min = 5, max = 500)
    private String diagnosis;

    @NotBlank(message = "O histórico familiar é obrigatório")
    @Size(min = 5, max = 500)
    private String familyHistory;

    @NotNull(message = "As alergias são obrigatórias")
    private List<Allergy> allergies;

    @NotNull(message = "As vacinas são obrigatórias")
    private List<Vaccine> vaccines;

    @NotNull(message = "O histórico cirúrgico é obrigatório")
    private List<Surgery> surgicalHistory;

    @NotNull(message = "A medicação é obrigatória")
    private List<Medication> medications;

    private boolean medicalRecordActive = true;

    /**
     * Converte uma entidade MedicalRecord em DTO de criação.
     * 
     * @param entity Entidade MedicalRecord a ser convertida
     * @return DTO com os dados do prontuário médico
     */
    public static MedicalRecordCreateDto fromEntity(MedicalRecord entity) {
        return MedicalRecordCreateDto.builder()
                .bloodType(entity.getBloodType())
                .organDonor(entity.getOrganDonor())
                .diagnosis(entity.getDiagnosis())
                .familyHistory(entity.getFamilyHistory())
                .allergies(entity.getAllergies())
                .vaccines(entity.getVaccines())
                .medications(entity.getMedications())
                .medicalRecordActive(entity.isMedicalRecordActive())
                .build();
    }

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
                .allergies(this.allergies)
                .vaccines(this.vaccines)
                .medications(this.medications)
                .medicalRecordActive(this.medicalRecordActive)
                .build();
    }
}
