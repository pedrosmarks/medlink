package br.fai.lds.medlink.mapper;

import br.fai.lds.medlink.domain.MedicalRecord;
import br.fai.lds.medlink.dto.MedicalRecordDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MedicalRecorMapper {

    MedicalRecordDto toDto(MedicalRecord entity);

    MedicalRecord toEntity(MedicalRecordDto dto);
}
