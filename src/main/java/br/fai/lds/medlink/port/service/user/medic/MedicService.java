package br.fai.lds.medlink.port.service.user.medic;

import br.fai.lds.medlink.domain.Medic;
import br.fai.lds.medlink.domain.Patient;
import br.fai.lds.medlink.port.service.crud.CrudService;

public interface MedicService extends CrudService<Medic> {

    Medic readById(int id);
}
