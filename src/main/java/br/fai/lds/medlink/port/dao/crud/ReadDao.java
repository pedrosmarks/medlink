package br.fai.lds.medlink.port.dao.crud;

import java.util.List;

public interface ReadDao<T> {

    T readyById(final int id);

    List<T> readAll();
}
