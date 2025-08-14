package br.fai.lds.medlink.implementation.dao;

import br.fai.lds.medlink.domain.Address;
import br.fai.lds.medlink.domain.Gender;
import br.fai.lds.medlink.domain.Medic;
import br.fai.lds.medlink.port.dao.medic.MedicDao;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Primary
@Repository
public class MedicFakeDaoImpl implements MedicDao {

    private static List<Medic> medics = new ArrayList<>();
    private static int ID = 1;

    private int getNextId() {
        return ID++;
    }

    public MedicFakeDaoImpl() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        medics.add(Medic.builder()
                .id(getNextId())
                .name("Dr. Pedro Almeida")
                .cpf("111.111.111-11")
                .password("123")
                .gender(Gender.MASCULINO)
                .birthDate(LocalDate.parse("15.03.1980", formatter))
                .phoneNumber("11 99999-8888")
                .address(Address.builder()
                        .street("Av. Paulista")
                        .number("1000")
                        .complement("Sala 101")
                        .neighborhood("Bela Vista")
                        .city("São Paulo")
                        .state("São Paulo")
                        .zipCode("01310-100")
                        .build())
                .crm("123456-SP")
                .specialty("Cardiologia")
                .email("pedro.almeida@medlink.com")
                .active(true)
                .build());

        medics.add(Medic.builder()
                .id(getNextId())
                .name("Dr. José Silva")
                .cpf("222.222.222-22")
                .password("123")
                .gender(Gender.MASCULINO)
                .birthDate(LocalDate.parse("20.07.1975", formatter))
                .phoneNumber("11 88888-7777")
                .address(Address.builder()
                        .street("Rua Augusta")
                        .number("500")
                        .complement("")
                        .neighborhood("Consolação")
                        .city("São Paulo")
                        .state("São Paulo")
                        .zipCode("01305-000")
                        .build())
                .crm("654321-SP")
                .specialty("Neurologia")
                .email("jose.silva@medlink.com")
                .active(true)
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
            return true;
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
    public List<Medic> readAll() {
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

    @Override
    public Medic findByEmail(String email) {
        return medics.stream()
                .filter(p -> p.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElse(null);
    }
}
