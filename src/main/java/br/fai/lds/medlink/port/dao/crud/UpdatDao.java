package br.fai.lds.medlink.port.dao.crud;

public interface UpdatDao<T> {

    void updateInformation(final int id, final T entity);
}
