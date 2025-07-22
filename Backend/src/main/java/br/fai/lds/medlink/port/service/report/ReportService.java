package br.fai.lds.medlink.port.service.report;


import br.fai.lds.medlink.domain.dataTransferObject.Report.BirthdayReportDto;
import br.fai.lds.medlink.domain.dataTransferObject.Report.ConsultationReportDto;
import br.fai.lds.medlink.domain.dataTransferObject.Report.PatientReportDto;
import br.fai.lds.medlink.domain.dataTransferObject.Report.PlanTypeReportDto;

import java.util.List;

// Interface que define os métodos para geração de relatórios do sistema,
public interface ReportService {
    List<BirthdayReportDto> getBirthdayReport();
    List<PlanTypeReportDto> getPlanTypeReport();
    List<ConsultationReportDto> getConsultationReport();
    List<PatientReportDto> getPatientReport();
}
