package br.fai.lds.medlink.controller;

import br.fai.lds.medlink.domain.*;
import br.fai.lds.medlink.domain.dataTransferObject.Patient.AuthorizedDoctorDto;
import br.fai.lds.medlink.port.service.medic.MedicService;
import br.fai.lds.medlink.port.service.patient.PatientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/patient")
@Slf4j
public class PatientPublicController {

    @Autowired
    private PatientService patientService;
    
    @Autowired
    private MedicService medicService;

    // Get authorized doctors for patient
    @GetMapping("/{patientId}/authorized-doctors")
    public ResponseEntity<ApiResponse<List<AuthorizedDoctorDto>>> getAuthorizedDoctors(@PathVariable int patientId) {
        try {
            Patient patient = patientService.findById(patientId);
            if (patient == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>("Paciente não encontrado."));
            }
            
            List<AuthorizedDoctorDto> authorizedDoctors = new ArrayList<>();
            
            if (patient.getEspecialistasAutorizados() != null) {
                for (EspecialistaAutorizado especialista : patient.getEspecialistasAutorizados()) {
                    Medic medic = medicService.findById(especialista.getMedicoId().intValue());
                    if (medic != null) {
                        authorizedDoctors.add(new AuthorizedDoctorDto(
                            medic.getId(),
                            medic.getName(),
                            medic.getSpecialty()
                        ));
                    }
                }
            }
            
            return ResponseEntity.ok(new ApiResponse<>("Médicos autorizados recuperados com sucesso.", authorizedDoctors));
        } catch (Exception e) {
            log.error("Erro ao buscar médicos autorizados do paciente ID {}: {}", patientId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Erro interno do servidor."));
        }
    }
}