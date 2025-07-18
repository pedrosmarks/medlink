package br.fai.lds.medlink.domain.dataTransferObject.MedicalRecord;

import br.fai.lds.medlink.domain.BloodType;
import br.fai.lds.medlink.domain.MedicalRecord;
import br.fai.lds.medlink.domain.OrganDonorStatus;
import br.fai.lds.medlink.domain.dataTransferObject.MedicalRecord.clinical.*;
import lombok.Builder;
import lombok.Data;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class MedicalRecordResponseDto {

    private int id;
    private int patientId;
    private BloodType bloodType;
    private OrganDonorStatus organDonor;
    private String diagnosis;
    private String familyHistory;
    private boolean medicalRecordActive;

    private List<AlergiaDto> alergias;
    private List<MedicamentoDto> medicamentos;
    private List<CirurgiaDto> cirurgias;
    private List<VacinaDto> vacinas;
    private List<ConsultaDto> consultas;

    public static MedicalRecordResponseDto fromEntity(MedicalRecord entity) {

        List<AlergiaDto> alergias = List.of();
        if (entity.getAllergies() != null && !entity.getAllergies().isBlank()) {
            alergias = Arrays.stream(entity.getAllergies().split(","))
                    .map(String::trim)
                    .map(nome -> AlergiaDto.builder().substancia(nome).build())
                    .collect(Collectors.toList());
        }

        List<MedicamentoDto> medicamentos = List.of();
        if (entity.getMedications() != null && !entity.getMedications().isBlank()) {
            medicamentos = Arrays.stream(entity.getMedications().split(","))
                    .map(String::trim)
                    .map(nome -> MedicamentoDto.builder().nome(nome).build())
                    .collect(Collectors.toList());
        }

        List<CirurgiaDto> cirurgias = List.of();
        if (entity.getSurgicalHistory() != null && !entity.getSurgicalHistory().isBlank()) {
            cirurgias = Arrays.stream(entity.getSurgicalHistory().split(","))
                    .map(String::trim)
                    .map(nome -> CirurgiaDto.builder()
                            .nome(nome)
                            .data(null)   // Aqui você pode ajustar para pegar a data real depois
                            .local(null)  // Aqui você pode ajustar para pegar o local real depois
                            .build())
                    .collect(Collectors.toList());
        }

        List<VacinaDto> vacinas = List.of();
        if (entity.getVaccine() != null && !entity.getVaccine().isBlank()) {
            vacinas = Arrays.stream(entity.getVaccine().split(","))
                    .map(String::trim)
                    .map(nome -> VacinaDto.builder()
                            .nome(nome)
                            .data(null)  // Ajustar futuramente para a data correta
                            .build())
                    .collect(Collectors.toList());
        }

        // Consultas ainda não armazenadas na entidade, retorna lista vazia por enquanto
        List<ConsultaDto> consultas = List.of();

        return MedicalRecordResponseDto.builder()
                .id(entity.getId())
                .patientId(entity.getPatientId())
                .bloodType(entity.getBloodType())
                .organDonor(entity.getOrganDonor())
                .diagnosis(entity.getDiagnosis())
                .familyHistory(entity.getFamilyHistory())
                .alergias(alergias)
                .medicamentos(medicamentos)
                .cirurgias(cirurgias)
                .vacinas(vacinas)
                .consultas(consultas)
                .medicalRecordActive(entity.isMedicalRecordActive())
                .build();
    }
}
