package br.fai.lds.medlink.implementation.service.mensagem;

import br.fai.lds.medlink.domain.Mensagem;
import br.fai.lds.medlink.implementation.dao.MensagesFakeDao;

import java.util.List;

public class MensagemServiceImpl {

    private final MensagesFakeDao fakeDao = new MensagesFakeDao();

    public List<Mensagem> listarTodas() {
        return fakeDao.listarTodas();
    }

    public void enviarMensagem(Mensagem mensagem) {
        fakeDao.adicionarMensagem(mensagem);
    }

    public Mensagem marcarComoLida(String id) {
        for (Mensagem m : fakeDao.listarTodas()) {
            if (m.getId().equals(id)) {
                m.setLida(true);
                return m;
            }
        }
        return null;
    }

    public List<Mensagem> buscarConversasPorUsuario(String remetenteId, String remetenteTipo) {
        return fakeDao.listarTodas().stream()
                .filter(m -> m.getRemetenteId().equals(remetenteId) && m.getRemetenteTipo().equals(remetenteTipo))
                .toList();
    }
}