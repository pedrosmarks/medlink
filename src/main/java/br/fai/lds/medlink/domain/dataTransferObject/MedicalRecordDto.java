package br.fai.lds.medlink.domain.dataTransferObject;

import br.fai.lds.medlink.domain.BloodType;
import br.fai.lds.medlink.domain.MedicalRecord;
import lombok.Data;

@Data
public class MedicalRecordDto {

    private int id;
    private BloodType bloodType;
    private String organDonor;
    private String diagnosis;
    private String familyHistory;
    private String allergies;
    private String vaccine;
    private String surgicalHistory;
    private String medications;

    public static MedicalRecordDto fromEntity(MedicalRecord entity) {
        MedicalRecordDto dto = new MedicalRecordDto();
        dto.setId(entity.getId());
        dto.setBloodType(entity.getBloodType());
        dto.setOrganDonor(entity.getOrganDonor());
        dto.setDiagnosis(entity.getDiagnosis());
        dto.setFamilyHistory(entity.getFamilyHistory());
        dto.setAllergies(entity.getAllergies());
        dto.setVaccine(entity.getVaccine());
        dto.setSurgicalHistory(entity.getSurgicalHistory());
        dto.setMedications(entity.getMedications());
        return dto;
    }

    // Converte de DTO para entidade
    public MedicalRecord toEntity() {
        MedicalRecord entity = new MedicalRecord();
        entity.setId(this.id);
        entity.setBloodType(this.bloodType);
        entity.setOrganDonor(this.organDonor);
        entity.setDiagnosis(this.diagnosis);
        entity.setFamilyHistory(this.familyHistory);
        entity.setAllergies(this.allergies);
        entity.setVaccine(this.vaccine);
        entity.setSurgicalHistory(this.surgicalHistory);
        entity.setMedications(this.medications);
        return entity;
    }

}
