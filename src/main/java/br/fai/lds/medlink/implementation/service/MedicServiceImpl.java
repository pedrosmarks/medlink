package br.fai.lds.medlink.implementation.service;

import br.fai.lds.medlink.domain.Medic;
import br.fai.lds.medlink.port.dao.user.MedicDao;
import br.fai.lds.medlink.port.service.user.medic.MedicService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicServiceImpl  implements MedicService {

    private final MedicDao medicDao;

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
        medicDao.remove(id);
        return Boolean.parseBoolean(null);
    }


    @Override
    public Medic findById(int id) {
        return (Medic) medicDao.readById(id);
    }

    @Override
    public List<Medic> findAll() {
        return medicDao.readAll();
    }

    @Override
    public Medic update(int id, Medic entity) {
        throw new UnsupportedOperationException("Update not implemented yet.");
    }
}
