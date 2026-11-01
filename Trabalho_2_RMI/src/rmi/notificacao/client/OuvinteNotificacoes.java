package rmi.notificacao.client;

import rmi.notificacao.api.OuvinteRemoto;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class OuvinteNotificacoes extends UnicastRemoteObject implements OuvinteRemoto {
    private static final long serialVersionUID = 1L;
    public OuvinteNotificacoes() throws RemoteException { super(); }
    @Override public void receberNotificacao(String json) { System.out.println("\n[NOTIFICACAO] " + json); }
}
