package br.fai.lds.medlink.port.service.patient;

import br.fai.lds.medlink.domain.Patient;
import br.fai.lds.medlink.port.service.crud.CrudService;

public interface PatientService extends CrudService<Patient> {
    boolean deactivate(int id);
    boolean delete(int id);
}
