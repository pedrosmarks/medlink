package br.fai.lds.medlink.port.dao.medic;

import br.fai.lds.medlink.domain.Medic;
import br.fai.lds.medlink.port.dao.crud.CrudDao;

public interface MedicDao extends CrudDao <Medic> {

    Medic findByEmail(String email);
}
