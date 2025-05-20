package br.fai.lds.medlink.mapper;

import br.fai.lds.medlink.domain.Medic;
import br.fai.lds.medlink.dataTransferObject.MedicDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MedicMapper {

    @Mapping(target = "id", expression = "java(java.util.concurrent.ThreadLocalRandom.current().nextLong(100_0000))")
    // Anotacao @Mapping é para uso temporario, enquanto não têm o banco de dados implementado, ela simula geracao de ID.

    MedicDto toDto (Medic entity);
    Medic toEntity(MedicDto dto);
}
