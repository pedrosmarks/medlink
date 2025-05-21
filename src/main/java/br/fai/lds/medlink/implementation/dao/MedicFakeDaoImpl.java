package br.fai.lds.medlink.implementation.dao;

import br.fai.lds.medlink.domain.Address;
import br.fai.lds.medlink.domain.Medic;
import br.fai.lds.medlink.port.dao.medic.MedicDao;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class MedicFakeDaoImpl implements MedicDao {

    private static List<Medic> medics = new ArrayList<>();
    private static int ID = 1;

    private int getNextId() {

        return ID++;
    }

    public MedicFakeDaoImpl() {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        medics.add(Medic.builder()
                .name("Olavo")
                .cpf("999.999.999-00")
                .gender(Gender.MASCULINO)
                .dataNascimento(LocalDate.parse("01.01.1990", formatter))
                .phoneNumber("8998654678")
                .address(Address.builder()
                        .street("Rua B")
                        .number("123")
                        .complement("")
                        .neighborhood("Faisqueira")
                        .city("Pouso Alegre")
                        .state("Minas Gerais")
                        .zipCode("89675634")
                        .build())
                .crm("MG-89")
                .specialty("Geral")
                .email("aumiau@gmail.com")
                .build());

    }


    @Override
    public void create(Medic entity) {
        entity.setId(getNextId());
        medics.add(entity);
    }

    @Override
    public boolean remove(int id) {
        Medic medic = readById(id);
        if (medic != null) {
            medic.setActive(false);
        }
        return false;
    }

    @Override
    public Medic readById(int id) {
        return medics.stream()
                .filter(medic -> medic.getId() == id)
                .findFirst()
                .orElse(null);
    }


    @Override
    public List readAll() {
        return medics;
    }

    @Override
    public void updateInformation(int id, Medic entity) {
        for (int i = 0; i < medics.size(); i++) {
            if (medics.get(i).getId() == id) {
                medics.set(i, entity);
                return;
            }
        }
    }
}
