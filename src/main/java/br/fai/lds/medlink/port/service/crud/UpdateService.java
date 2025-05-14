package br.fai.lds.medlink.port.service.crud;

public interface UpdateService <T>{

    void update(final int id, final T entity);
}
