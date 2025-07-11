package br.fai.lds.medlink.domain.dataTransferObject.MedicalRecord;

import br.fai.lds.medlink.domain.BloodType;
import br.fai.lds.medlink.domain.MedicalRecord;
import br.fai.lds.medlink.domain.OrganDonorStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class MedicalRecordUpdateDto {

    @NotNull(message = "O tipo sanguíneo é obrigatório")
    private BloodType bloodType;

    @NotNull(message = "A informação de doador de órgãos é obrigatória")
    private OrganDonorStatus organDonor;

    @NotBlank(message = "O diagnóstico é obrigatório")
    @Size(min = 5, max = 500)
    private String diagnosis;

    @NotBlank(message = "O histórico familiar é obrigatório")
    @Size(min = 5, max = 500)
    private String familyHistory;

    @NotBlank(message = "As alergias são obrigatórias")
    @Size(min = 5, max = 500)
    private String allergies;

    @NotBlank(message = "As vacinas são obrigatórias")
    @Size(min = 2, max = 500)
    private String vaccine;

    @NotBlank(message = "O histórico cirúrgico é obrigatório")
    @Size(min = 5, max = 500)
    private String surgicalHistory;

    @NotBlank(message = "A medicação é obrigatória")
    @Size(min = 5, max = 500)
    private String medications;

    public void updateEntity(MedicalRecord entity) {
        entity.setBloodType(bloodType);
        entity.setOrganDonor(organDonor);
        entity.setDiagnosis(diagnosis);
        entity.setFamilyHistory(familyHistory);
        entity.setAllergies(allergies);
        entity.setVaccine(vaccine);
        entity.setSurgicalHistory(surgicalHistory);
        entity.setMedications(medications);
    }
}
