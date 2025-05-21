package br.fai.lds.medlink.mapper;

import br.fai.lds.medlink.domain.Patient;
import br.fai.lds.medlink.domain.dataTransferObject.PatientDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PatientMapper {

    @Mapping(target = "id", expression = "java(java.util.concurrent.ThreadLocalRandom.current().nextLong(100_0000))")
    // Anotacao @Mapping é para uso temporario, enquanto não têm o banco de dados implementado, ela simula geracao de ID.

    PatientDto toDto (Patient entity);
    Patient toEntity(PatientDto dto);
}
