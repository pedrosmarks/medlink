package br.fai.lds.medlink.port.dao.crud;

public interface SoftDeleteDao {
    boolean deactivate(int id);
}
