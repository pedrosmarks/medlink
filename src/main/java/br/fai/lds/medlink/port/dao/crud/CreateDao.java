package br.fai.lds.medlink.port.dao.crud;

public interface CreateDao<T> {

    void create(final T entity);
}
