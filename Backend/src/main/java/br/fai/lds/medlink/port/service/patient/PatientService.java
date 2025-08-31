package br.fai.lds.medlink.port.service.patient;

import br.fai.lds.medlink.domain.Patient;
import br.fai.lds.medlink.domain.dataTransferObject.Patient.PatientResponseDto;
import br.fai.lds.medlink.port.service.crud.CrudService;

import java.util.List;

public interface PatientService extends CrudService<Patient> {
    boolean remove(int id);

    Patient findByEmail(String email);

    boolean updateInformation(int id, Patient entity);

    boolean deactivate(int id);
    boolean delete(int id);
    List<PatientResponseDto> getPatientsByMedicId(int medicId);
    List<Patient> findByMedicId(int medicId);
}
