package rmi.notificacao.model;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/** Composicao: o canal agrega as notificacoes publicadas. */
public class CanalNotificacoes {
    private final List<Notificacao> historico = new CopyOnWriteArrayList<>();
    public void registrar(Notificacao notificacao) { historico.add(notificacao); }
    public List<Notificacao> getHistorico() { return new CopyOnWriteArrayList<>(historico); }
}
