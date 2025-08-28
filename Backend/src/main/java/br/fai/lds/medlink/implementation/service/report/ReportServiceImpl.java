package br.fai.lds.medlink.implementation.service.report;

import br.fai.lds.medlink.domain.dataTransferObject.Report.BirthdayReportDto;
import br.fai.lds.medlink.domain.dataTransferObject.Report.ConsultationReportDto;
import br.fai.lds.medlink.domain.dataTransferObject.Report.PatientReportDto;
import br.fai.lds.medlink.domain.dataTransferObject.Report.PlanTypeReportDto;
import br.fai.lds.medlink.port.dao.patient.PatientDao;
import br.fai.lds.medlink.port.service.report.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl implements ReportService {
    
    @Autowired
    private PatientDao patientDao;

    @Override
    public List<Map<String, String>> getAvailableReportTypes() {
        return List.of(
            Map.of(
                "id", "1",
                "icone", "https://cdn-icons-png.flaticon.com/512/3135/3135715.png",
                "titulo", "Relatório de atendimentos",
                "descricao", "Resumo dos atendimentos realizados no mês."
            ),
            Map.of(
                "id", "2",
                "icone", "https://cdn-icons-png.flaticon.com/512/3135/3135715.png",
                "titulo", "Relatório financeiro",
                "descricao", "Resumo financeiro mensal."
            ),
            Map.of(
                "id", "3",
                "icone", "https://cdn-icons-png.flaticon.com/512/3135/3135715.png",
                "titulo", "Relatório de aniversariantes",
                "descricao", "Lista de pacientes aniversariantes do mês."
            ),
            Map.of(
                "id", "4",
                "icone", "https://cdn-icons-png.flaticon.com/512/3135/3135715.png",
                "titulo", "Relatório por tipo de plano",
                "descricao", "Distribuição de pacientes por tipo de plano."
            )
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