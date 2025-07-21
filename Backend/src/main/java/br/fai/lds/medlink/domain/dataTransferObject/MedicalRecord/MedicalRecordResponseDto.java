package br.fai.lds.medlink.domain.dataTransferObject.MedicalRecord;

import br.fai.lds.medlink.domain.BloodType;
import br.fai.lds.medlink.domain.MedicalRecord;
import br.fai.lds.medlink.domain.OrganDonorStatus;
import br.fai.lds.medlink.domain.dataTransferObject.MedicalRecord.clinical.*;
import lombok.Builder;
import lombok.Data;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class MedicalRecordResponseDto {

    private int id;
    private int patientId;
    private BloodType bloodType;
    private OrganDonorStatus organDonor;
    private String diagnosis;
    private String familyHistory;
    private boolean medicalRecordActive;

    private List<AllergyCreateDto> allergies;
    private List<MedicationCreateDto> medications;
    private List<SurgeryCreateDto> surgeries;
    private List<VaccineCreateDto> vaccines;
    private List<ConsultationCreateDto> consultations;

    public static MedicalRecordResponseDto fromEntity(MedicalRecord entity) {

        List<AllergyCreateDto> allergies = List.of();
        if (entity.getAllergies() != null && !entity.getAllergies().isEmpty()) {
            allergies = entity.getAllergies().stream()
                    .map(allergy -> AllergyCreateDto.builder()
                            .substance(allergy.getSubstance())
                            .build())
                    .collect(Collectors.toList());
        }

        List<MedicationCreateDto> medications = List.of();
        if (entity.getMedications() != null && !entity.getMedications().isEmpty()) {
            medications = entity.getMedications().stream()
                    .map(med -> MedicationCreateDto.builder()
                            .name(med.getName())
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
                .vaccines(vaccines)
                .consultations(consultations)
                .build();
    }
}