package br.fai.lds.medlink.domain.dataTransferObject.MedicalRecord;

import br.fai.lds.medlink.domain.BloodType;
import br.fai.lds.medlink.domain.MedicalRecord;
import br.fai.lds.medlink.domain.OrganDonorStatus;
import br.fai.lds.medlink.domain.dataTransferObject.MedicalRecord.Allergy.AllergyCreateDto;
import br.fai.lds.medlink.domain.dataTransferObject.MedicalRecord.Consultation.ConsultationCreateDto;
import br.fai.lds.medlink.domain.dataTransferObject.MedicalRecord.Medication.MedicationCreateDto;
import br.fai.lds.medlink.domain.dataTransferObject.MedicalRecord.Surgery.SurgeryCreateDto;
import br.fai.lds.medlink.domain.dataTransferObject.MedicalRecord.Vaccine.VaccineCreateDto;
import lombok.Builder;
import lombok.Data;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * DTO para resposta com dados completos do prontuário médico.
 * 
 * <p>Utilizado para retornar informações detalhadas do prontuário médico,
 * incluindo todos os dados clínicos e históricos do paciente.</p>
 * 
 * @author MedLink Team
 * @version 1.0
 * @since 1.0
 */
@Data
@Builder
public class MedicalRecordResponseDto {

    /**
     * ID único do prontuário médico.
     */
    private int id;
    
    /**
     * ID do paciente proprietário do prontuário.
     */
    private Integer patientId;
    
    /**
     * Tipo sanguíneo do paciente.
     */
    private BloodType bloodType;
    
    /**
     * Status de doador de órgãos.
     */
    private OrganDonorStatus organDonor;
    
    /**
     * Diagnóstico médico principal.
     */
    private String diagnosis;
    
    /**
     * Histórico familiar de doenças.
     */
    private String familyHistory;
    
    /**
     * Indica se o prontuário está ativo.
     */
    private boolean medicalRecordActive;

    /**
     * Lista de alergias do paciente.
     */
    private List<AllergyCreateDto> allergies;
    
    /**
     * Lista de medicações em uso.
     */
    private List<MedicationCreateDto> medications;
    
    /**
     * Histórico de cirurgias realizadas.
     */
    private List<SurgeryCreateDto> surgeries;
    
    /**
     * Histórico de vacinação.
     */
    private List<VaccineCreateDto> vaccines;
    
    /**
     * Histórico de consultas médicas.
     */
    private List<ConsultationCreateDto> consultations;

    /**
     * Cria um DTO de resposta a partir de uma entidade MedicalRecord.
     * 
     * @param entity Entidade MedicalRecord a ser convertida
     * @return DTO com os dados do prontuário médico formatados para resposta
     */
    public static MedicalRecordResponseDto fromEntity(MedicalRecord entity) {

        List<AllergyCreateDto> allergies = List.of();
        if (entity.getAllergies() != null && !entity.getAllergies().isEmpty()) {
            allergies = entity.getAllergies().stream()
                    .map(allergy -> AllergyCreateDto.builder()
                            .name(allergy.getName())
                            .substance(allergy.getSubstance())
                            .reaction(allergy.getReaction())
                            .severity(allergy.getSeverity())
                            .build())
                    .collect(Collectors.toList());
        }

        List<MedicationCreateDto> medications = List.of();
        if (entity.getMedications() != null && !entity.getMedications().isEmpty()) {
            medications = entity.getMedications().stream()
                    .map(med -> MedicationCreateDto.builder()
                            .name(med.getName())
                            .dosage(med.getDosage())
                            .frequency(med.getFrequency())
                            .build())
                    .collect(Collectors.toList());
        }

        List<VaccineCreateDto> vaccines = List.of();
        if (entity.getVaccines() != null && !entity.getVaccines().isEmpty()) {
            vaccines = entity.getVaccines().stream()
                    .map(vaccine -> VaccineCreateDto.builder()
                            .name(vaccine.getName())
                            .date(vaccine.getDate())
                            .build())
                    .collect(Collectors.toList());
        }

        List<ConsultationCreateDto> consultations = List.of();
        if (entity.getConsultations() != null && !entity.getConsultations().isEmpty()) {
            consultations = entity.getConsultations().stream()
                    .map(consult -> ConsultationCreateDto.builder()
                            .date(consult.getDate())
                            .reason(consult.getReason())
                            .notes(consult.getNotes())
                            .build())
                    .collect(Collectors.toList());
        }

        List<SurgeryCreateDto> surgeries = List.of();
        if (entity.getSurgeries() != null && !entity.getSurgeries().isEmpty()) {
            surgeries = entity.getSurgeries().stream()
                    .map(surgery -> SurgeryCreateDto.builder()
                            .name(surgery.getName())
                            .date(surgery.getDate())
                            .location(surgery.getLocation())
                            .notes(surgery.getNotes())
                            .build())
                    .collect(Collectors.toList());
        }

        return MedicalRecordResponseDto.builder()
                .id(entity.getId())
                .patientId(entity.getPatientId())
                .bloodType(entity.getBloodType())
                .organDonor(entity.getOrganDonor())
                .diagnosis(entity.getDiagnosis())
                .familyHistory(entity.getFamilyHistory())
                .medicalRecordActive(entity.isMedicalRecordActive())
                .allergies(allergies)
                .medications(medications)
                .surgeries(surgeries)
                .vaccines(vaccines)
                .consultations(consultations)
                .build();
    }
}