package br.fai.lds.medlink.dataTransferObject;

import br.fai.lds.medlink.domain.BloodType;
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

}
