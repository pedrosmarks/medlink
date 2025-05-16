package br.fai.lds.medlink.port.dao.user;

import br.fai.lds.medlink.domain.Patient;
import br.fai.lds.medlink.port.dao.crud.CrudDao;

public interface PatientDao extends CrudDao <Patient> {
    Patient readById(int id);
}
