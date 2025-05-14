package br.fai.lds.medlink.port.service;

public interface CreateService <T> {

    int create(final T entity);
}
