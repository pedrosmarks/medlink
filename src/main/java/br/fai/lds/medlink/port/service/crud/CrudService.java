package br.fai.lds.medlink.port.service.crud;

public interface CrudService <T> extends
        CreateService<T>,
        DeactivateService,
        ReadService<T>,
        UpdateService<T> {
}
