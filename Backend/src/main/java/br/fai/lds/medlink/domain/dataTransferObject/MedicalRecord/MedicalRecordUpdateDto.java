package br.fai.lds.medlink.domain.dataTransferObject.MedicalRecord;

import br.fai.lds.medlink.domain.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicalRecordUpdateDto {
    private BloodType bloodType;
    private OrganDonorStatus organDonor;
    
    @Size(min = 5, max = 500, message = "Diagnóstico deve ter entre 5 e 500 caracteres")
    private String diagnosis;
    
    @Size(min = 5, max = 500, message = "Histórico familiar deve ter entre 5 e 500 caracteres")
    private String familyHistory;
    
    private List<Allergy> allergies;
    private List<Vaccine> vaccines;
    private List<Surgery> surgeries;
    private List<Medication> medications;
    private List<Consultation> consultations;
    private Boolean medicalRecordActive;

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
