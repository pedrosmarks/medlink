package br.fai.lds.medlink.mapper;

import br.fai.lds.medlink.domain.MedicalRecord;
import br.fai.lds.medlink.dataTransferObject.MedicalRecordDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MedicalRecordMapper {

    MedicalRecordDto toDto(MedicalRecord entity);

    MedicalRecord toEntity(MedicalRecordDto dto);
}
