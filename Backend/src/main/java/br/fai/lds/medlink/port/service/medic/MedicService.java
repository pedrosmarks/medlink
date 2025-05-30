package br.fai.lds.medlink.port.service.medic;

import br.fai.lds.medlink.domain.Medic;
import br.fai.lds.medlink.port.service.crud.CrudService;

public interface MedicService extends CrudService<Medic> {

    Medic readById(int id);
}
