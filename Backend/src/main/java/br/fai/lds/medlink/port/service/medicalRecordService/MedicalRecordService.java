package br.fai.lds.medlink.port.service.medicalRecordService;

import br.fai.lds.medlink.domain.*;
import br.fai.lds.medlink.domain.dataTransferObject.MedicalRecord.MedicalRecordResponseDto;
import br.fai.lds.medlink.domain.dataTransferObject.MedicalRecord.MedicalRecordUpdateDto;
import br.fai.lds.medlink.port.service.crud.CrudService;

public interface MedicalRecordService extends CrudService<MedicalRecord> {



        MedicalRecord readById(int id);

        MedicalRecord update(int id, MedicalRecordUpdateDto dto);

        MedicalRecordResponseDto findByPatientId(int medicId, int patientId);

        void addConsultation(int medicalRecordId, Consultation consultation);

        void addMedication(int medicalRecordId, Medication medication);

        void addAllergy(int medicalRecordId, Allergy allergy);

        void addVaccine(int medicalRecordId, Vaccine vaccine);

        void addSurgery(int medicalRecordId, Surgery surgery);

        void addDiagnosis(int medicalRecordId, Diagnosis diagnosis);

        void addDiagnosis(int id, String diagnosis);

        void addFamilyHistory(int id, String familyHistory);
}

