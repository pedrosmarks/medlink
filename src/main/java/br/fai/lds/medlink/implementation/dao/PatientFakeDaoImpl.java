package br.fai.lds.medlink.implementation.dao;

import br.fai.lds.medlink.domain.Address;
import br.fai.lds.medlink.domain.Gender;
import br.fai.lds.medlink.domain.Patient;
import br.fai.lds.medlink.port.dao.user.PatientDao;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;



public class PatientFakeDaoImpl implements PatientDao {


    private static List<Patient> patients = new ArrayList<>();
    private static int ID=1;

    private int getNextId(){
       return ID++;
    }


    public PatientFakeDaoImpl(){


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        patients.add(Patient.builder()
                .id(getNextId())
                .name("Bolota")
                .cpf("123.456.789-10")
                .gender(Gender.FEMININO)
                .dataNascimento(LocalDate.parse("01.12.2019",formatter))
                .phoneNumber("Liga pra mamãe e pro papai")
                .address(Address.builder()
                        .street("Rua A")
                        .number("123")
                        .complement("")
                        .neighborhood("centro")
                        .city("Cambuí")
                        .state("Minas Gerais")
                        .zipCode("123456789")
                        .build())
                        .email("bolotinha@gmail.com")
                        .plan("Pet")
                        .susCard("123456")
                .build());
        patients.add(Patient.builder()
                .id(getNextId())
                .name("Jade")
                .cpf("456.789.123-45")
                .gender(Gender.FEMININO)
                .dataNascimento(LocalDate.parse("12.03.2019",formatter))
                .phoneNumber("Liga pra mamãe e pro papai")
                .address(Address.builder()
                        .street("Rua A")
                        .number("123")
                        .complement("")
                        .neighborhood("centro")
                        .city("Ouro Fino")
                        .state("Minas Gerais")
                        .zipCode("123456789")
                        .build())
                .email("jadinha@gmail.com")
                .plan("Pet")
                .susCard("123456")
                .build());

        patients.add(Patient.builder()
                .id(getNextId())
                .name("Frajola")
                .cpf("789.123.456-78")
                .gender(Gender.FEMININO)
                .dataNascimento(LocalDate.parse("01.12.2019",formatter))
                .phoneNumber("Liga pra mamãe e pro papai")
                .address(Address.builder()
                        .street("Rua A")
                        .number("123")
                        .complement("")
                        .neighborhood("centro")
                        .city("Santa Rita do Sapucaí")
                        .state("Minas Gerais")
                        .zipCode("123456789")
                        .build())
                .email("frajolinha@gmail.com")
                .plan("Pet")
                .susCard("123456")
                .build());
    }

    @Override
    public void create(Object entity) {

    }

    @Override
    public void remove(int id) {

    }

    @Override
    public Object readyById(int id) {

        return patients.stream()
                .filter(patient -> patient.getId() == id)
                .findFirst()
                .orElse(null);
    }


    @Override
    public List readAll() {
        return patients;
    }

    @Override
    public void updateInformation(int id, Object entity) {

    }
}
