package br.fai.lds.medlink.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class MedicalRecord {

    @NotNull(message = "O tipo sanguíneo é obrigatório")
    private BloodType bloodType;

    @NotBlank(message = "A informação de doador de órgãos é obrigatória")
    private String organDonor;

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
    @Size(min = 5, max = 500, message = "As vacinas devem ter entre 5 e 500 caracteres")
    private String vaccine;

    @NotBlank(message = "O histórico cirúrgico é obrigatório")
    @Size(min = 5, max = 500, message = "O histórico cirúrgico deve ter entre 5 e 500 caracteres")
    private String surgicalHistory;

    @NotBlank(message = "A medicação é obrigatória")
    @Size(min = 5, max = 500, message = "A medicação deve ter entre 5 e 500 caracteres")
    private String medications;
}

