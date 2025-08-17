package br.fai.lds.medlink.implementation.dao;

import br.fai.lds.medlink.domain.Mensagem;

import java.util.ArrayList;
import java.util.List;

public class MensagesFakeDao {

    private final List<Mensagem> mensagens = new ArrayList<>();

    public MensagesFakeDao() {
        mensagens.add(new Mensagem(
                "1", "1", "paciente", "João da Silva",
                "medico_1", "medico", "Dr. Carlos Silva",
                "Olá, doutor! Gostaria de agendar uma consulta.",
                "2024-08-04T10:00:00", false
        ));
        mensagens.add(new Mensagem(
                "2", "medico_1", "medico", "Dr. Carlos Silva",
                "1", "paciente", "João da Silva",
                "Olá, João! Claro, vamos agendar. Que dia seria melhor para você?",
                "2024-08-04T10:05:00", false
        ));
        mensagens.add(new Mensagem(
                "44de", "1", "paciente", "João da Silva",
                "medico_1", "medico", "Dr. Carlos Silva",
                "Você tá bom?",
                "2025-08-05T23:45:02.221Z", false
        ));
        mensagens.add(new Mensagem(
                "1e91", "1", "paciente", "João da Silva",
                "medico_1", "medico", "Dr. Carlos Silva",
                "Sim doutor estou bom demais",
                "2025-08-05T23:45:10.358Z", false
        ));
        mensagens.add(new Mensagem(
                "49d4", "1", "paciente", "João da Silva",
                "medico_1", "medico", "Dr. Carlos Silva",
                "queria uma ajuda",
                "2025-08-05T23:45:15.356Z", false
        ));
        mensagens.add(new Mensagem(
                "2716", "1", "paciente", "João da Silva",
                "medico_1", "medico", "Dr. Carlos Silva",
                "opaaa",
                "2025-08-06T16:07:58.197Z", false
        ));
    }

    public List<Mensagem> listarTodas() {
        return new ArrayList<>(mensagens);
    }

    public void adicionarMensagem(Mensagem mensagem) {
        mensagens.add(mensagem);
    }
}