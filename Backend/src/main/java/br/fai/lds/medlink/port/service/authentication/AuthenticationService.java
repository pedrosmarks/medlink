package br.fai.lds.medlink.port.service.authentication;

import br.fai.lds.medlink.domain.Patient;
import br.fai.lds.medlink.domain.Medic;

public interface AuthenticationService {

    Patient authenticatePatient (final String email, final String password);
    Medic authenticateMedic (final String email, final String password);
}
