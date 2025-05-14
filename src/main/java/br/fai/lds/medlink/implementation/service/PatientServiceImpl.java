package br.fai.lds.medlink.implementation.service;

import br.fai.lds.medlink.domain.Patient;
import br.fai.lds.medlink.port.service.patient.PatientService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientServiceImpl implements PatientService {
    @Override
    public int create(Patient entity) {
        return 0;
    }

    @Override
    public void delete(int id) {

    }

    @Override
    public Patient findById(int id) {
        return null;
    }

    @Override
    public List<Patient> findAll() {
        return List.of();
    }

    @Override
    public void update(int id, Patient entity) {

    }
}
