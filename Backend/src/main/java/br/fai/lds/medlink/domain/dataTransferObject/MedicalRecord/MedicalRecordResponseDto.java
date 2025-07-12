package br.fai.lds.medlink.domain.dataTransferObject.MedicalRecord;

import br.fai.lds.medlink.domain.BloodType;
import br.fai.lds.medlink.domain.MedicalRecord;
import br.fai.lds.medlink.domain.OrganDonorStatus;
import lombok.Data;

@Data
public class MedicalRecordResponseDto {

    private int id;
    private BloodType bloodType;
    private OrganDonorStatus organDonor;
    private String diagnosis;
    private String familyHistory;
    private String allergies;
    private String vaccine;
    private String surgicalHistory;
    private String medications;
    private boolean medicalRecordActive;

    public static MedicalRecordResponseDto fromEntity(MedicalRecord record) {
        MedicalRecordResponseDto dto = new MedicalRecordResponseDto();
        dto.setId(record.getId());
        dto.setBloodType(record.getBloodType());
        dto.setOrganDonor(record.getOrganDonor());
        dto.setDiagnosis(record.getDiagnosis());
        dto.setFamilyHistory(record.getFamilyHistory());
        dto.setAllergies(record.getAllergies());
        dto.setVaccine(record.getVaccine());
        dto.setSurgicalHistory(record.getSurgicalHistory());
        dto.setMedications(record.getMedications());
        dto.setMedicalRecordActive(record.isMedicalRecordActive());
        return dto;
    }
}
