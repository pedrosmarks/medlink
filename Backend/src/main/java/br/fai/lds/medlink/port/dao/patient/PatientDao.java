package br.fai.lds.medlink.port.dao.patient;

import br.fai.lds.medlink.domain.Patient;
import br.fai.lds.medlink.port.dao.crud.CrudDao;
import br.fai.lds.medlink.port.dao.crud.SoftDeleteDao;

import java.util.List;

public interface PatientDao extends CrudDao <Patient>, SoftDeleteDao {

    Patient readById(int id);
    Patient findByEmail(String email);
    List<Patient> findByMedicId(int medicId);
    List<Patient> findAll();



}
