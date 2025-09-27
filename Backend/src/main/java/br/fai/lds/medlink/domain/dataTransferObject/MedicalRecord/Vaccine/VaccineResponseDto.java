package br.fai.lds.medlink.domain.dataTransferObject.MedicalRecord.Vaccine;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VaccineResponseDto {
    private String name;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private String date;
    private boolean softDeleted;

    public static VaccineResponseDto fromEntity(br.fai.lds.medlink.domain.Vaccine entity) {
        return VaccineResponseDto.builder()
                .name(entity.getName())
                .date(String.valueOf(entity.getDate()))
                .softDeleted(entity.isSoftDeleted())
                .build();
    }
}
