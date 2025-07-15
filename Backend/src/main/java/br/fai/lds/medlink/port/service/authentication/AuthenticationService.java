package br.fai.lds.medlink.port.service.authentication;

import br.fai.lds.medlink.domain.Patient;
import br.fai.lds.medlink.domain.Medic;
import br.fai.lds.medlink.domain.dataTransferObject.Login.PasswordResetRequestDTO;
import br.fai.lds.medlink.domain.dataTransferObject.Login.PasswordResetDTO;

public interface AuthenticationService {

    Patient authenticatePatient(final String email, final String password);
    Medic authenticateMedic(final String email, final String password);

    void requestPasswordReset(PasswordResetRequestDTO dto);
    boolean resetPassword(PasswordResetDTO dto);
}

