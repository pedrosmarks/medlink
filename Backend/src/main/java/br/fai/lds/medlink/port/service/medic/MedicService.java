package br.fai.lds.medlink.port.service.medic;

import br.fai.lds.medlink.domain.Medic;
import br.fai.lds.medlink.port.service.crud.CrudService;

import java.util.List;
import java.util.Map;

public interface MedicService extends CrudService<Medic> {

    /**
     * Busca múltiplos médicos por IDs de uma vez para evitar N+1 queries
     * @param ids Lista de IDs dos médicos
     * @return Map com ID como chave e Medic como valor
     */
    Map<Integer, Medic> findByIds(List<Integer> ids);

}
