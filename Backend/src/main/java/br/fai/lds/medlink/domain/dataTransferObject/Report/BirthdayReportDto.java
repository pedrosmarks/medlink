package br.fai.lds.medlink.domain.dataTransferObject.Report;

import lombok.Builder;
import lombok.Data;

// DTO utilizado para representar os dados do relatório de aniversários pacientes
@Data
@Builder
public class BirthdayReportDto {
    private String patientName;
    private String birthDate;
}