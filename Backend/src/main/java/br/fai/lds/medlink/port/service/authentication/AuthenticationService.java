package br.fai.lds.medlink.port.service.authentication;

import br.fai.lds.medlink.domain.Patient;
import br.fai.lds.medlink.domain.Medic;

public interface AuthenticationService {

    Patient authenticate (final String email, final String password);
    Medic authenticate (final String email, final String password);
}
