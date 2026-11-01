package rmi.notificacao.api;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServicoNotificacoes extends Remote {
    String entrar(String clienteJson, OuvinteRemoto ouvinte) throws RemoteException;
    String sair(String nomeCliente) throws RemoteException;
    String publicar(String administradorJson, String notificacaoJson) throws RemoteException;
    String listarHistorico() throws RemoteException;
    String status() throws RemoteException;
}
