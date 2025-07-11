package br.fai.lds.medlink.domain.dataTransferObject.MedicalRecord;


import br.fai.lds.medlink.domain.BloodType;
import br.fai.lds.medlink.domain.MedicalRecord;
import br.fai.lds.medlink.domain.OrganDonorStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicalRecordCreateDto {

    private int id;

    @NotNull(message = "O tipo sanguíneo é obrigatório")
    private BloodType bloodType;

    @NotNull(message = "O status de doador de órgãos é obrigatório")
    private OrganDonorStatus organDonor;

    @NotBlank(message = "O diagnóstico é obrigatório")
    @Size(min = 5, max = 500, message = "O diagnóstico deve ter entre 5 e 500 caracteres")
    private String diagnosis;

    @NotBlank(message = "O histórico familiar é obrigatório")
    @Size(min = 5, max = 500, message = "O histórico familiar deve ter entre 5 e 500 caracteres")
    private String familyHistory;

    @NotBlank(message = "As alergias são obrigatórias")
    @Size(min = 5, max = 500, message = "As alergias devem ter entre 5 e 500 caracteres")
    private String allergies;

    @NotBlank(message = "As vacinas são obrigatórias")
    @Size(min = 2, max = 500, message = "As vacinas devem ter entre 2 e 500 caracteres")
    private String vaccine;

    @NotBlank(message = "O histórico cirúrgico é obrigatório")
    @Size(min = 5, max = 500, message = "O histórico cirúrgico deve ter entre 5 e 500 caracteres")
    private String surgicalHistory;

    @NotBlank(message = "A medicação é obrigatória")
    @Size(min = 5, max = 500, message = "A medicação deve ter entre 5 e 500 caracteres")
    private String medications;

    private boolean medicalRecordActive = true;

    /**
     * Converte uma entidade para DTO.
     */
    public static MedicalRecordCreateDto fromEntity(MedicalRecord entity) {
        return MedicalRecordCreateDto.builder()
                .id(entity.getId())
                .bloodType(entity.getBloodType())
                .organDonor(entity.getOrganDonor())
                .diagnosis(entity.getDiagnosis())
                .familyHistory(entity.getFamilyHistory())
                .allergies(entity.getAllergies())
                .vaccine(entity.getVaccine())
                .surgicalHistory(entity.getSurgicalHistory())
                .medications(entity.getMedications())
                .medicalRecordActive(entity.isMedicalRecordActive())
                .build();
    }

    /**
     * Converte o DTO para entidade.
     */
    public MedicalRecord toEntity() {
        return MedicalRecord.builder()
                .id(this.id)
                .bloodType(this.bloodType)
                .organDonor(this.organDonor)
                .diagnosis(this.diagnosis)
                .familyHistory(this.familyHistory)
                .allergies(this.allergies)
                .vaccine(this.vaccine)
                .surgicalHistory(this.surgicalHistory)
                .medications(this.medications)
                .medicalRecordActive(this.medicalRecordActive)
                .build();
    }
}
