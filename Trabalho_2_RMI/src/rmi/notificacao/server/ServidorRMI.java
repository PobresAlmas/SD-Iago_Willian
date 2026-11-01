package rmi.notificacao.server;

import rmi.notificacao.api.ServicoNotificacoes;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public final class ServidorRMI {
    public static final int PORTA = 1099;
    public static final String NOME_SERVICO = "ServicoNotificacoes";
    private ServidorRMI() { }

    public static void main(String[] args) throws Exception {
        Registry registry = LocateRegistry.createRegistry(PORTA);
        ServicoNotificacoes servico = new ServicoNotificacoesImpl();
        registry.rebind(NOME_SERVICO, servico);
        System.out.println("Servico RMI pronto na porta " + PORTA + ". Ctrl+C encerra.");
    }
}
