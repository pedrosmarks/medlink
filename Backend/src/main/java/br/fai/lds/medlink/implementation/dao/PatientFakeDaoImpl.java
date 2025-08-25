package br.fai.lds.medlink.implementation.dao;

import br.fai.lds.medlink.domain.*;
import br.fai.lds.medlink.port.dao.patient.PatientDao;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Primary
@Repository
public class PatientFakeDaoImpl implements PatientDao {

    private static List<Patient> patientList = new ArrayList<>();
    private static int ID = 1;
    private static boolean initialized = false; // Flag para evitar duplicação

    private int getNextId() {
        return ID++;
    }

    public PatientFakeDaoImpl() {
        // Só inicializa se ainda não foi inicializado
        if (!initialized) {
            initializeData();
            initialized = true;
        }
    }

    private void initializeData() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        // Paciente 1 - João da Silva
        patientList.add(Patient.builder()
                .id(getNextId())
                .name("João da Silva")
                .cpf("xxx.xxx.xxx-xx")
                .password("123")
                .gender(Gender.MASCULINO)
                .birthDate(LocalDate.parse("01.01.1994", formatter))
                .phoneNumber("35 9xxxx-xxxx")
                .avatar("https://cdn-icons-png.flaticon.com/512/921/921347.png")
                .bloodType("A+")
                .observations("")
                .address(Address.builder()
                        .street("Rua A")
                        .number("123")
                        .complement("")
                        .neighborhood("centro")
                        .city("Cambuí")
                        .state("Minas Gerais")
                        .zipCode("12345-678")
                        .build())
                .email("joao@exemplo.com.br")
                .plan("Particular")
                .susCard("123456")
                .medicId(1)
                .active(true)
                .especialistasAutorizados(new ArrayList<>(List.of(
                        new EspecialistaAutorizado(1L),
                        new EspecialistaAutorizado(2L)
                )))
                .requisicoesAcesso(new ArrayList<>(List.of(
                        new RequisicaoAcesso(3, "aprovado")
                )))
                .consultas(new ArrayList<>(List.of(
                        new Consulta("2024-06-10", "Consulta de rotina")
                )))
                .vacinas(new ArrayList<>(List.of(
                        new Vacina("COVID-19", "2023-01-15"),
                        new Vacina("Febre amarela", "2025-08-06")
                )))
                .medicamentos(new ArrayList<>(List.of(
                        new Medicamento("Losartana", "50mg", null),
                        new Medicamento("Tylenol", "2 por dia", "2025-08-07")
                )))
                .cirurgias(new ArrayList<>(List.of(
                        new Cirurgia("Apendicectomia", "2015-08-20")
                )))
                .diagnosticos(new ArrayList<>(List.of(
                        new Diagnostico("Hipertensão", "2022-03-01")
                )))
                .alergias(new ArrayList<>(List.of(
                        new Alergia("top")
                )))
                .build());

        // Paciente 2 - Maria Oliveira
        patientList.add(Patient.builder()
                .id(getNextId())
                .name("Maria Oliveira")
                .cpf("yyy.yyy.yyy-yy")
                .password("123")
                .gender(Gender.FEMININO)
                .birthDate(LocalDate.parse("15.05.1979", formatter))
                .phoneNumber("11 9xxxx-xxxx")
                .avatar("https://cdn-icons-png.flaticon.com/512/921/921347.png")
                .bloodType("B-")
                .observations("Paciente diabética")
                .address(Address.builder()
                        .street("Rua B")
                        .number("456")
                        .complement("Apto 101")
                        .neighborhood("Vila Nova")
                        .city("São Paulo")
                        .state("São Paulo")
                        .zipCode("01234-567")
                        .build())
                .email("maria@exemplo.com.br")
                .plan("Convênio")
                .susCard("654321")
                .medicId(2)
                .active(true)
                .especialistasAutorizados(new ArrayList<>(List.of(
                        new EspecialistaAutorizado(2L)
                )))
                .requisicoesAcesso(new ArrayList<>())
                .consultas(new ArrayList<>(List.of(
                        new Consulta("2024-05-20", "Avaliação de rotina")
                )))
                .vacinas(new ArrayList<>(List.of(
                        new Vacina("Influenza", "2023-03-10")
                )))
                .medicamentos(new ArrayList<>(List.of(
                        new Medicamento("Metformina", "850mg", null)
                )))
                .cirurgias(new ArrayList<>())
                .diagnosticos(new ArrayList<>(List.of(
                        new Diagnostico("Diabetes Tipo 2", "2020-09-15")
                )))
                .alergias(new ArrayList<>(List.of(
                        new Alergia("Penicilina")
                )))
                .build());

        // Paciente 3 - Carlos Pereira
        patientList.add(Patient.builder()
                .id(getNextId())
                .name("Carlos Pereira")
                .cpf("zzz.zzz.zzz-zz")
                .password("123")
                .gender(Gender.MASCULINO)
                .birthDate(LocalDate.parse("15.05.1972", formatter))
                .phoneNumber("21 9xxxx-xxxx")
                .avatar("https://cdn-icons-png.flaticon.com/512/921/921347.png")
                .bloodType("O+")
                .observations("Paciente com histórico de cirurgia cardíaca")
                .address(Address.builder()
                        .street("Rua C")
                        .number("789")
                        .complement("")
                        .neighborhood("Centro")
                        .city("Rio de Janeiro")
                        .state("Rio de Janeiro")
                        .zipCode("20000-000")
                        .build())
                .email("carlos@exemplo.com.br")
                .plan("Convênio")
                .susCard("789123")
                .medicId(1)
                .active(true)
                .especialistasAutorizados(new ArrayList<>(List.of(
                        new EspecialistaAutorizado(1L)
                )))
                .requisicoesAcesso(new ArrayList<>())
                .consultas(new ArrayList<>(List.of(
                        new Consulta("2024-04-15", "Pós-operatório")
                )))
                .vacinas(new ArrayList<>(List.of(
                        new Vacina("Hepatite B", "2022-11-05")
                )))
                .medicamentos(new ArrayList<>(List.of(
                        new Medicamento("AAS", "100mg", null)
                )))
                .cirurgias(new ArrayList<>(List.of(
                        new Cirurgia("Revascularização do miocárdio", "2023-12-01")
                )))
                .diagnosticos(new ArrayList<>(List.of(
                        new Diagnostico("Cardiopatia isquêmica", "2023-10-20")
                )))
                .alergias(new ArrayList<>())
                .build());
    }

    // Resto dos métodos permanecem iguais...
    @Override
    public void create(Patient entity) {
        entity.setId(getNextId());
        patientList.add(entity);
    }

    @Override
    public boolean remove(int id) {
        Patient patient = readById(id);
        if (patient != null) {
            patient.setActive(false);
            return true;
        }
        return false;
    }

    @Override
    public Patient readById(int id) {
        return patientList.stream()
                .filter(patient -> patient.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public Patient findByEmail(String email) {
        return patientList.stream()
                .filter(p -> p.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Patient> readAll() {
        return new ArrayList<>(patientList);
    }

    @Override
    public void updateInformation(int id, Patient entity) {
        for (int i = 0; i < patientList.size(); i++) {
            if (patientList.get(i).getId() == id) {
                // Manter o ID original
                entity.setId(id);
                patientList.set(i, entity);
                return;
            }
        }
    }

    @Override
    public boolean deactivate(int id) {
        Patient patient = readById(id);
        if (patient == null) return false;

        patient.setActive(false);
        updateInformation(id, patient);
        return true;
    }

    @Override
    public List<Patient> findByMedicId(int medicId) {
        return patientList.stream()
                .filter(patient -> patient.getMedicId() == medicId)
                .collect(Collectors.toList());
    }

    @Override
    public List<Patient> findAll() {
        return patientList;
    }
}