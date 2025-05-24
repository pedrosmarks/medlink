package br.fai.lds.medlink.port.service.crud;

import br.fai.lds.medlink.domain.Patient;

public interface UpdateService <T>{

     T update(final int id, final T entity);
}
