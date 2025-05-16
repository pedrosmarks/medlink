package br.fai.lds.medlink.port.service.crud;

import br.fai.lds.medlink.domain.Medic;

public interface UpdateService <T>{

    Medic update(final int id, final T entity);
}
