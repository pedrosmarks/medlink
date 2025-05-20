package br.fai.lds.medlink.dto;

import br.fai.lds.medlink.domain.BloodType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class MedicalRecordDto {


    private BloodType bloodType;
    private String organDonor;
    private String diagnosis;
    private String familyHistory;
    private String allergies;
    private String vaccine;
    private String surgicalHistory;
    private String medications;


}
