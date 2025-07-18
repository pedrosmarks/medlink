package br.fai.lds.medlink.domain.dataTransferObject.MedicalRecord;

import br.fai.lds.medlink.domain.BloodType;
import br.fai.lds.medlink.domain.OrganDonorStatus;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MedicalRecordResponseDto {

    private int id;
    private int patientId;
    private BloodType bloodType;
    private OrganDonorStatus organDonor;
    private String diagnosis;
    private String familyHistory;
    private String allergies;
    private String vaccine;
    private String surgicalHistory;
    private String medications;
    private boolean medicalRecordActive;

    public static MedicalRecordResponseDto fromEntity(br.fai.lds.medlink.domain.MedicalRecord record) {
        return MedicalRecordResponseDto.builder()
                .id(record.getId())
                .patientId(record.getPatientId())
                .bloodType(record.getBloodType())
                .organDonor(record.getOrganDonor())
                .diagnosis(record.getDiagnosis())
                .familyHistory(record.getFamilyHistory())
                .allergies(record.getAllergies())
                .vaccine(record.getVaccine())
                .surgicalHistory(record.getSurgicalHistory())
                .medications(record.getMedications())
                .medicalRecordActive(record.isMedicalRecordActive())
                .build();
    }
}
