package br.fai.lds.medlink.port.dao.crud;

public interface CrudDao <T> extends CreateDao<T>, DeactivateDao,ReadDao<T>, UpdateDao<T> {
}
