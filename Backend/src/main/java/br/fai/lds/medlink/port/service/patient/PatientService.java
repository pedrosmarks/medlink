package br.fai.lds.medlink.port.service.patient;

import br.fai.lds.medlink.domain.Patient;
import br.fai.lds.medlink.domain.Consultation;
import br.fai.lds.medlink.domain.dataTransferObject.Patient.PatientResponseDto;
import br.fai.lds.medlink.port.service.crud.CrudService;

import java.util.List;

public interface PatientService extends CrudService<Patient> {
    void updateAccessRequestStatus(int patientId, int medicoId, String status);
    void authorizeSpecialist(int patientId, int medicoId);
    boolean remove(int id);

    Patient findByEmail(String email);

    boolean updateInformation(int id, Patient entity);

    boolean deactivate(int id);
    boolean delete(int id);
    List<PatientResponseDto> getPatientsByMedicId(int medicId);
    List<Patient> findByMedicId(int medicId);
    void sendAccessRequest(int patientId, int medicoId);

    void revokeDoctorAccess(int patientId, int medicoId);
    Consultation addConsultation(int patientId, Consultation consultation);
    List<Consultation> getConsultationsByPatientId(int patientId);

    boolean deleteConsultation(int patientId, int consultationId);
    boolean deleteMedication(int patientId, int medicationId);
    boolean deleteVaccine(int patientId, int vaccineId);
    boolean deleteAllergy(int patientId, int allergyId);
    boolean deleteDiagnosis(int patientId, int diagnosisId);
    boolean deleteSurgery(int patientId, int surgeryId);
}
