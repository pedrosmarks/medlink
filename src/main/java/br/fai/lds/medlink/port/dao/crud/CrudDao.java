package br.fai.lds.medlink.port.dao.crud;

public interface CrudDao <T> extends CreateDao<T>,DeleteDao,ReadDao<T>, UpdateDao<T> {
}
