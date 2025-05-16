package br.fai.lds.medlink.implementation.service;

import br.fai.lds.medlink.domain.Patient;
import br.fai.lds.medlink.port.dao.user.PatientDao;
import br.fai.lds.medlink.port.service.user.patient.PatientService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientServiceImpl implements PatientService {

    private final PatientDao patientDao;

    public PatientServiceImpl(PatientDao patientDao) {
        this.patientDao = patientDao;
    }

    @Override
    public int create(Patient entity) {
        patientDao.create(entity);
        return entity.getId();
    }

    @Override
    public void delete(int id) {
        patientDao.remove(id);
    }

    @Override
    public Patient findById(int id) {
        return (Patient) patientDao.readById(id);
    }

    @Override
    public List<Patient> findAll() {
        return patientDao.readAll();
    }

    @Override
    public Patient update(int id, Patient entity) {
        throw new UnsupportedOperationException("Update not implemented yet.");
    }
}

