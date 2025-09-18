package br.fai.lds.medlink.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

/**
 * Entidade que representa um paciente no sistema MedLink.
 * 
 * <p>Contém todas as informações pessoais, médicas e de acesso do paciente,
 * incluindo histórico médico completo, especialistas autorizados e solicitações de acesso.</p>
 * 
 * @author MedLink Team
 * @version 1.0
 * @since 1.0
 */
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class Patient {
    /** ID único do paciente no sistema. */
    private int id;
    
    /** Nome completo do paciente. */
    private String name;
    
    /** CPF do paciente (documento de identificação). */
    private String cpf;
    
    /** Senha de acesso do paciente ao sistema. */
    private String password;
    
    /** Gênero do paciente (MASCULINO, FEMININO, OUTRO). */
    private Gender gender;
    
    /** Data de nascimento do paciente. */
    private LocalDate birthDate;
    
    /** Idade do paciente (calculada ou informada). */
    private Integer age;
    
    /** Número de telefone para contato. */
    private String phoneNumber;
    
    /** URL ou caminho para foto do paciente. */
    private String avatar;
    
    /** Tipo sanguíneo do paciente (A+, B+, AB+, O+, A-, B-, AB-, O-). */
    private String bloodType;
    
    /** Observações gerais sobre o paciente. */
    private String observations;
    
    /** Endereço residencial do paciente. */
    private Address address;
    
    /** Email do paciente para contato e acesso. */
    private String email;
    
    /** Plano de saúde ou convênio médico. */
    private String plan;
    
    /** Número do cartão SUS. */
    private String susCard;
    
    /** ID do médico responsável (se houver). */
    private Integer medicId;
    
    /** Indica se o paciente está ativo no sistema. */
    private boolean active;
    
    /** Lista de especialistas autorizados a acessar o prontuário. */
    private List<EspecialistaAutorizado> especialistasAutorizados;
    
    /** Lista de solicitações de acesso ao prontuário. */
    private List<RequisicaoAcesso> requisicoesAcesso;
    
    /** Histórico de consultas médicas. */
    private List<Consultation> consultations;
    
    /** Histórico de vacinação. */
    private List<Vaccine> vacinas;
    
    /** Lista de medicações em uso ou já utilizadas. */
    private List<Medication> medications;
    
    /** Histórico de cirurgias realizadas. */
    private List<Surgery> cirurgias;
    
    /** Lista de diagnósticos médicos. */
    private List<Diagnosis> diagnosticos;
    
    /** Lista de alergias conhecidas. */
    private List<Allergy> alergias;

    public Patient() {}

    public Patient(int id, String name, String cpf, String password, Gender gender,
                   LocalDate birthDate, Integer age, String phoneNumber, String avatar, String bloodType,
                   String observations, Address address, String email, String plan,
                   String susCard, Integer medicId, boolean active,
                   List<EspecialistaAutorizado> especialistasAutorizados,
                   List<RequisicaoAcesso> requisicoesAcesso, List<Consultation> consultations,
                   List<Vaccine> vacinas, List<Medication> medications,
                   List<Surgery> cirurgias, List<Diagnosis> diagnosticos,
                   List<Allergy> alergias) {
        this.id = id;
        this.name = name;
        this.cpf = cpf;
        this.password = password;
        this.gender = gender;
        this.birthDate = birthDate;
        this.age = age;
        this.phoneNumber = phoneNumber;
        this.avatar = avatar;
        this.bloodType = bloodType;
        this.observations = observations;
        this.address = address;
        this.email = email;
        this.plan = plan;
        this.susCard = susCard;
        this.medicId = medicId;
        this.active = active;
        this.especialistasAutorizados = especialistasAutorizados;
        this.requisicoesAcesso = requisicoesAcesso;
        this.consultations = consultations;
        this.vacinas = vacinas;
        this.medications = medications;
        this.cirurgias = cirurgias;
        this.diagnosticos = diagnosticos;
        this.alergias = alergias;
    }
}