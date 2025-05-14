package br.fai.lds.medlink.port.service.crud;

public interface CreateService <T> {

    int create(final T entity);
}
