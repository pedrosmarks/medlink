package br.fai.lds.medlink.implementation.dao;

import br.fai.lds.medlink.domain.Address;
import br.fai.lds.medlink.domain.Gender;
import br.fai.lds.medlink.domain.Patient;
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

    private int getNextId() {
        return ID++;
    }

    public PatientFakeDaoImpl() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        patientList.add(Patient.builder()
                .id(getNextId())
                .name("João da Silva")
                .cpf("123.456.789-10")
                .password("123")
                .gender(Gender.MASCULINO)
                .birthDate(LocalDate.parse("01.01.1990", formatter))
                .phoneNumber("35 99999-9999")
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
                .build());

        patientList.add(Patient.builder()
                .id(getNextId())
                .name("Maria Oliveira")
                .cpf("987.654.321-00")
                .password("123")
                .gender(Gender.FEMININO)
                .birthDate(LocalDate.parse("15.05.1985", formatter))
                .phoneNumber("11 88888-8888")
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
                .medicId(1)
                .active(true)
                .build());
    }

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
