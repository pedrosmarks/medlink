package br.fai.lds.medlink.port.service.report;

import br.fai.lds.medlink.domain.dataTransferObject.Report.BirthdayReportDto;
import br.fai.lds.medlink.domain.dataTransferObject.Report.ConsultationReportDto;
import br.fai.lds.medlink.domain.dataTransferObject.Report.PatientReportDto;
import br.fai.lds.medlink.domain.dataTransferObject.Report.PlanTypeReportDto;
import java.util.List;
import java.util.Map;

public interface ReportService {
    List<Map<String, String>> getAvailableReportTypes();
    List<BirthdayReportDto> getBirthdayReport();
    List<PlanTypeReportDto> getPlanTypeReport();
    List<ConsultationReportDto> getConsultationReport();
    List<PatientReportDto> getPatientReport();
}
