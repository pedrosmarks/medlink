package br.fai.lds.medlink.domain.dataTransferObject.Patient;

import br.fai.lds.medlink.domain.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

/**
 * DTO para resposta com dados completos do paciente.
 * 
 * <p>Utilizado para retornar informações detalhadas do paciente, incluindo
 * dados pessoais, médicos e relacionamentos com especialistas.</p>
 * 
 * @author MedLink Team
 * @version 1.0
 * @since 1.0
 */
@Data
public class PatientResponseDto {
    private int id;
    private String name;
    private String avatar;
    private String cpf;
    private Gender gender;
    private LocalDate birthDate;
    private Integer age;
    private String bloodType;
    private String phoneNumber;
    private String email;
    private Address address;
    private String observations;
    private String plan;
    private String susCard;
    private Integer medicId;
    private boolean active;
    private List<Integer> authorizedSpecialists;
    private List<RequisicaoAcesso> accessRequests;
    private List<Consultation> consultations;
    private List<Vaccine> vaccines;
    private List<Medication> medications;
    private List<Surgery> surgeries;
    private List<Diagnosis> diagnoses;
    private List<Allergy> allergies;

    /**
     * Cria um DTO de resposta a partir de uma entidade Patient.
     * 
     * @param entity Entidade Patient a ser convertida
     * @return DTO com os dados do paciente formatados para resposta
     */
    public static PatientResponseDto fromEntity(Patient entity) {
        PatientResponseDto dto = new PatientResponseDto();

        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setAvatar(entity.getAvatar());
        dto.setCpf(entity.getCpf());
        dto.setGender(entity.getGender());
        dto.setBirthDate(entity.getBirthDate());

        dto.setAge(calculateAge(entity.getBirthDate()));

        dto.setBloodType(entity.getBloodType());
        dto.setPhoneNumber(entity.getPhoneNumber());
        dto.setEmail(entity.getEmail());
        dto.setAddress(entity.getAddress());
        dto.setObservations(entity.getObservations());
        dto.setPlan(entity.getPlan());
        dto.setSusCard(entity.getSusCard());
        dto.setMedicId(entity.getMedicId());
        dto.setActive(entity.isActive());

        // Mapear diretamente das entidades
        dto.setAuthorizedSpecialists(
                entity.getEspecialistasAutorizados() != null ?
                        entity.getEspecialistasAutorizados().stream()
                                .map(esp -> esp.getMedicoId().intValue())
                                .collect(Collectors.toList()) : List.of()
        );

        dto.setAccessRequests(entity.getRequisicoesAcesso() != null ? entity.getRequisicoesAcesso() : List.of());
        dto.setConsultations(entity.getConsultations() != null ? entity.getConsultations() : List.of());
        dto.setVaccines(entity.getVacinas() != null ? entity.getVacinas() : List.of());
        dto.setMedications(entity.getMedications() != null ? entity.getMedications() : List.of());
        dto.setSurgeries(entity.getCirurgias() != null ? entity.getCirurgias() : List.of());
        dto.setDiagnoses(entity.getDiagnosticos() != null ? entity.getDiagnosticos() : List.of());
        dto.setAllergies(entity.getAlergias() != null ? entity.getAlergias() : List.of());

        return dto;
    }

    /**
     * Calcula a idade com base na data de nascimento.
     * 
     * @param birthDate Data de nascimento
     * @return Idade em anos ou null se birthDate for null
     */
    private static Integer calculateAge(LocalDate birthDate) {
        if (birthDate == null) {
            return null;
        }
        return Period.between(birthDate, LocalDate.now()).getYears();
    }
}