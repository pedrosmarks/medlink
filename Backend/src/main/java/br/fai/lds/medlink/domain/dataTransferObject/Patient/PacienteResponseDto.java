package br.fai.lds.medlink.domain.dataTransferObject.Patient;

import br.fai.lds.medlink.domain.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class PacienteResponseDto {
    private Long id;
    private String name;
    private String avatar;
    private String cpf;
    private Integer idade;
    private String tipoSanguineo;
    private String telefone;
    private String email;
    private Address address;
    private String observacoes;
    private List<Long> especialistasAutorizados;
    private List<RequisicaoAcesso> requisicoesAcesso;
    private List<Consultation> consultations;
    private List<Vaccine> vacinas;
    private List<Medicamento> medicamentos;
    private List<Surgery> cirurgias;
    private List<Diagnosis> diagnosticos;
    private List<Allergy> alergias;

    public static PacienteResponseDto fromEntity(Patient entity) {
        PacienteResponseDto dto = new PacienteResponseDto();

        dto.setId((long) entity.getId());
        dto.setName(entity.getName());
        dto.setAvatar(entity.getAvatar());
        dto.setCpf(entity.getCpf());

        Integer idade = null;
        if (entity.getBirthDate() != null) {
            idade = Period.between(entity.getBirthDate(), LocalDate.now()).getYears();
        }
        dto.setIdade(idade);

        dto.setTipoSanguineo(entity.getBloodType());
        dto.setTelefone(entity.getPhoneNumber());
        dto.setEmail(entity.getEmail());
        dto.setAddress(entity.getAddress());
        dto.setObservacoes(entity.getObservations());

        // Mapear diretamente das entidades
        dto.setEspecialistasAutorizados(
                entity.getEspecialistasAutorizados() != null ?
                        entity.getEspecialistasAutorizados().stream()
                                .map(EspecialistaAutorizado::getMedicoId)
                                .collect(Collectors.toList()) : List.of()
        );

        dto.setRequisicoesAcesso(entity.getRequisicoesAcesso() != null ? entity.getRequisicoesAcesso() : List.of());
        dto.setConsultations(entity.getConsultations() != null ? entity.getConsultations() : List.of());
        dto.setVacinas(entity.getVacinas() != null ? entity.getVacinas() : List.of());
        dto.setMedicamentos(entity.getMedicamentos() != null ? entity.getMedicamentos() : List.of());
        dto.setCirurgias(entity.getCirurgias() != null ? entity.getCirurgias() : List.of());
        dto.setDiagnosticos(entity.getDiagnosticos() != null ? entity.getDiagnosticos() : List.of());
        dto.setAlergias(entity.getAlergias() != null ? entity.getAlergias() : List.of());

        return dto;
    }
}