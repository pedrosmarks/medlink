package br.fai.lds.medlink.implementation.service.authentication;

import br.fai.lds.medlink.domain.Medic;
import br.fai.lds.medlink.domain.Patient;
import br.fai.lds.medlink.port.dao.medic.MedicDao;
import br.fai.lds.medlink.port.dao.patient.PatientDao;
import br.fai.lds.medlink.port.service.authentication.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class AuthenticationServiceImpl implements AuthenticationService {


    @Autowired
    private PatientDao patientDao;
    @Autowired
    private MedicDao medicDao;


    @Override
    public Patient authenticatePatient(String email, String password) {
        Patient patient = patientDao.findByEmail(email);
        if(patient != null && patient.getPassword().equals(password) ){
            return patient;

        }
        return null;
    }

    @Override
    public Medic authenticateMedic(String email, String password) {
        Medic medic = medicDao.findByEmail(email);
        if (medic != null && medic.getPassword().equals(password)) {
            return medic;

        }
        return null;
    }
}
