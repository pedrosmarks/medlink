package br.fai.lds.medlink.port.service;

public interface CrudService <T> extends CreateService<T>,DeleteService, ReadService<T>, UpdateService<T> {
}
