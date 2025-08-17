package br.fai.lds.medlink.implementation.service.pacient;

import br.fai.lds.medlink.domain.Patient;
import br.fai.lds.medlink.domain.dataTransferObject.Patient.PatientResponseDto;
import br.fai.lds.medlink.port.dao.patient.PatientDao;
import br.fai.lds.medlink.port.service.patient.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientServiceImpl implements PatientService {

    @Autowired
    private PatientDao patientDao;

    @Override
    public int create(Patient entity) {
        patientDao.create(entity);
        return 0;
    }

    @Override
    public boolean remove(int id) {
        return patientDao.remove(id);
    }

    @Override
    public Patient findById(int id) {
        return patientDao.readById(id);
    }

    @Override
    public Patient findByEmail(String email) {
        return patientDao.findByEmail(email);
    }

    @Override
    public List<Patient> findAll() {
        return patientDao.findAll();
    }

    @Override
    public void updateInformation(int id, Patient entity) {
        patientDao.updateInformation(id, entity);
    }

    @Override
    public boolean deactivate(int id) {
        return patientDao.deactivate(id);
    }

    @Override
    public boolean delete(int id) {
        return false;
    }

    @Override
    public List<PatientResponseDto> getPatientsByMedicId(int medicId) {
        return List.of();
    }

    @Override
    public List<Patient> findByMedicId(int medicId) {
        return patientDao.findByMedicId(medicId);
    }

    // Implementação do método update
    @Override
    public Patient update(int id, Patient entity) {
        entity.setId(id); // Garantir que o ID seja mantido
        patientDao.updateInformation(id, entity);
        return patientDao.readById(id);
    }
}