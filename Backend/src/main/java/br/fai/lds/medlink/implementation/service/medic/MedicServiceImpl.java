package br.fai.lds.medlink.implementation.service.medic;

import br.fai.lds.medlink.domain.Medic;
import br.fai.lds.medlink.port.dao.medic.MedicDao;
import br.fai.lds.medlink.port.service.medic.MedicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicServiceImpl implements MedicService {

    private final MedicDao medicDao;

    @Autowired
    public MedicServiceImpl(MedicDao medicDao) {
        this.medicDao = medicDao;
    }

    @Override
    public int create(Medic entity) {
        medicDao.create(entity);
        return entity.getId();
    }

    @Override
    public boolean delete(int id) {
        Medic medic = medicDao.readById(id);
        if (medic == null) {
            return false;
        }
        medic.setActive(false);
        medicDao.updateInformation(id, medic);
        return true;
    }

    @Override
    public Medic findById(int id) {
        return medicDao.readById(id);
    }

    @Override
    public List<Medic> findAll() {
        return medicDao.readAll();
    }

    @Override
    public Medic update(int id, Medic entity) {
        Medic existingMedic = medicDao.readById(id);
        if (existingMedic == null) {
            throw new IllegalArgumentException("Médico com o id " + id + " não foi encontrado");
        }

        existingMedic.setName(entity.getName());
        existingMedic.setGender(entity.getGender());
        existingMedic.setBirthDate(entity.getBirthDate());
        existingMedic.setPhoneNumber(entity.getPhoneNumber());
        existingMedic.setAddress(entity.getAddress());
        existingMedic.setCrm(entity.getCrm());
        existingMedic.setSpecialty(entity.getSpecialty());
        existingMedic.setEmail(entity.getEmail());
        existingMedic.setActive(entity.isActive());

        medicDao.updateInformation(id, existingMedic);

        return existingMedic;
    }
}
