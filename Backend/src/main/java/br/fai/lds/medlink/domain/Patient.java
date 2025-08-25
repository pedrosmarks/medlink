package br.fai.lds.medlink.domain;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class Patient {
    private int id;
    private String name;
    private String cpf;
    private String password;
    private Gender gender;
    private LocalDate birthDate;
    private String phoneNumber;
    private String avatar;
    private String bloodType;
    private String observations;
    private Address address;
    private String email;
    private String plan;
    private String susCard;
    private int medicId;
    private boolean active;
    private List<EspecialistaAutorizado> especialistasAutorizados;
    private List<RequisicaoAcesso> requisicoesAcesso;
    private List<Consulta> consultas;
    private List<Vaccine> vacinas;
    private List<Medicamento> medicamentos;
    private List<Surgery> cirurgias;
    private List<Diagnostico> diagnosticos;
    private List<Allergy> alergias;

    public Patient() {}

    public Patient(int id, String name, String cpf, String password, Gender gender,
                   LocalDate birthDate, String phoneNumber, String avatar, String bloodType,
                   String observations, Address address, String email, String plan,
                   String susCard, int medicId, boolean active,
                   List<EspecialistaAutorizado> especialistasAutorizados,
                   List<RequisicaoAcesso> requisicoesAcesso, List<Consulta> consultas,
                   List<Vaccine> vacinas, List<Medicamento> medicamentos,
                   List<Surgery> cirurgias, List<Diagnostico> diagnosticos,
                   List<Allergy> alergias) {
        this.id = id;
        this.name = name;
        this.cpf = cpf;
        this.password = password;
        this.gender = gender;
        this.birthDate = birthDate;
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
        this.consultas = consultas;
        this.vacinas = vacinas;
        this.medicamentos = medicamentos;
        this.cirurgias = cirurgias;
        this.diagnosticos = diagnosticos;
        this.alergias = alergias;
    }
}