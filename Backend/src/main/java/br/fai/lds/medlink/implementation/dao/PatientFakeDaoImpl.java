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
                .name("Bolota")
                .cpf("123.456.789-10")
                .password("1243")
                .gender(Gender.FEMININO)
                .birthDate(LocalDate.parse("01.12.2019", formatter))
                .phoneNumber("Liga pra mamãe e pro papai")
                .address(Address.builder()
                        .street("Rua A")
                        .number("123")
                        .complement("")
                        .neighborhood("centro")
                        .city("Cambuí")
                        .state("Minas Gerais")
                        .zipCode("12345-678")
                        .build())
                .email("bolotinha@gmail.com")
                .plan("Pet")
                .susCard("123456")
                .medicId(1) // IMPORTANTE: define o ID do médico vinculado
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


}
