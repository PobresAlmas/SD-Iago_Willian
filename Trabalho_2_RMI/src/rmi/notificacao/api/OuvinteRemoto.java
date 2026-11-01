package rmi.notificacao.api;

import java.rmi.Remote;
import java.rmi.RemoteException;

/** Callback passado por referencia ao servidor RMI. */
public interface OuvinteRemoto extends Remote {
    void receberNotificacao(String notificacaoJson) throws RemoteException;
}
