package br.fai.lds.medlink.port.service.medicalRecordService;

import br.fai.lds.medlink.domain.MedicalRecord;
import br.fai.lds.medlink.port.service.crud.CrudService;

public interface MedicalRecordService extends CrudService<MedicalRecord> {

        MedicalRecord readById(int id);

}
