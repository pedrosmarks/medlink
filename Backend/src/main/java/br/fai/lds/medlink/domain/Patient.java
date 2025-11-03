package br.fai.lds.medlink.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import java.time.LocalDate;
import java.util.List;

/**
 * Entidade que representa um paciente no sistema MedLink.
 * 
 * <p>Extende a classe Person e adiciona informações específicas do paciente,
 * incluindo histórico médico completo, especialistas autorizados e solicitações de acesso.</p>
 * 
 * @author MedLink Team
 * @version 1.0
 * @since 1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Patient extends Person {
    /** Idade do paciente (calculada ou informada). */
    private Integer age;
    
    /** URL ou caminho para foto do paciente. */
    private String avatar;
    
    /** Tipo sanguíneo do paciente (A+, B+, AB+, O+, A-, B-, AB-, O-). */
    private String bloodType;
    
    /** Observações gerais sobre o paciente. */
    private String observations;
    
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


}