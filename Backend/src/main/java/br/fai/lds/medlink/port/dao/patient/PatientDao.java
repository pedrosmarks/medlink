package br.fai.lds.medlink.port.dao.patient;

import br.fai.lds.medlink.domain.Patient;
import br.fai.lds.medlink.port.dao.crud.CrudDao;
import br.fai.lds.medlink.port.dao.crud.SoftDeleteDao;

public interface PatientDao extends CrudDao <Patient>, SoftDeleteDao {

    Patient readById(int id);


}
