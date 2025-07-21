package br.fai.lds.medlink.domain;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class MedicalRecord {

    private int id;

    @NotNull(message = "O tipo sanguíneo é obrigatório")
    private BloodType bloodType;

    @NotNull(message = "A informação de doador de órgãos é obrigatória")
    private OrganDonorStatus organDonor;

    @NotNull(message = "O diagnóstico é obrigatório")
    private String diagnosis;

    @NotNull(message = "O histórico familiar é obrigatório")
    private String familyHistory;

    private boolean medicalRecordActive = true;

    private int patientId;

    @Builder.Default
    private List<Allergy> allergies = new ArrayList<>();

    @Builder.Default
    private List<Vaccine> vaccines = new ArrayList<>();

    @Builder.Default
    private List<Surgery> surgeries = new ArrayList<>();

    @Builder.Default
    private List<Medication> medications = new ArrayList<>();

    @Builder.Default
    private List<Consultation> consultations = new ArrayList<>();
}
