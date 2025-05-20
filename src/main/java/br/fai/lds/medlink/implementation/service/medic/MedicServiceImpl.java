package br.fai.lds.medlink.implementation.service.medic;

import br.fai.lds.medlink.domain.Medic;
import br.fai.lds.medlink.port.dao.medic.MedicDao;
import br.fai.lds.medlink.port.service.medic.MedicService;
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

    @Override
    public Medic readById(int id) {
        return null;
    }
}
