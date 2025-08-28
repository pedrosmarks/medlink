package br.fai.lds.medlink.implementation.service.report;

import br.fai.lds.medlink.domain.dataTransferObject.Report.BirthdayReportDto;
import br.fai.lds.medlink.domain.dataTransferObject.Report.ConsultationReportDto;
import br.fai.lds.medlink.domain.dataTransferObject.Report.PatientReportDto;
import br.fai.lds.medlink.domain.dataTransferObject.Report.PlanTypeReportDto;
import br.fai.lds.medlink.domain.dataTransferObject.Report.ReportTypeDto;
import br.fai.lds.medlink.port.dao.patient.PatientDao;
import br.fai.lds.medlink.port.service.report.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl implements ReportService {
    
    @Autowired
    private PatientDao patientDao;

    @Override
    public List<ReportTypeDto> getAvailableReportTypes() {
        return List.of(
            new ReportTypeDto("1", "https://cdn-icons-png.flaticon.com/512/3135/3135715.png", 
                "Relatório de atendimentos", "Resumo dos atendimentos realizados no mês."),
            new ReportTypeDto("2", "https://cdn-icons-png.flaticon.com/512/3135/3135715.png", 
                "Relatório financeiro", "Resumo financeiro mensal."),
            new ReportTypeDto("3", "https://cdn-icons-png.flaticon.com/512/3135/3135715.png", 
                "Relatório de aniversariantes", "Lista de pacientes aniversariantes do mês."),
            new ReportTypeDto("4", "https://cdn-icons-png.flaticon.com/512/3135/3135715.png", 
                "Relatório por tipo de plano", "Distribuição de pacientes por tipo de plano.")
        );
    }

    @Override
    public List<BirthdayReportDto> getBirthdayReport() {
        return patientDao.findAll().stream()
                .map(p -> BirthdayReportDto.builder()
                        .patientName(p.getName())
                        .birthDate(p.getBirthDate().toString())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public List<PlanTypeReportDto> getPlanTypeReport() {
        return patientDao.findAll().stream()
                .map(p -> PlanTypeReportDto.builder()
                        .patientName(p.getName())
                        .planType(p.getPlan())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public List<ConsultationReportDto> getConsultationReport() {
        return List.of();
    }

    @Override
    public List<PatientReportDto> getPatientReport() {
        return patientDao.findAll().stream()
                .map(p -> PatientReportDto.builder()
                        .name(p.getName())
                        .email(p.getEmail())
                        .plan(p.getPlan())
                        .active(p.isActive())
                        .build())
                .collect(Collectors.toList());
    }
}