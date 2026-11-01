package rmi.notificacao.client;

import rmi.notificacao.api.ServicoNotificacoes;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public final class ClienteRMI {
    private ClienteRMI() { }
    public static void main(String[] args) throws Exception {
        String host = args.length > 0 ? args[0] : "localhost";
        Registry registry = LocateRegistry.getRegistry(host, 1099);
        ServicoNotificacoes servico = (ServicoNotificacoes) registry.lookup("ServicoNotificacoes");
        Scanner entrada = new Scanner(System.in);
        System.out.print("Seu nome: ");
        String nome = entrada.nextLine().trim();
        OuvinteNotificacoes ouvinte = new OuvinteNotificacoes();
        System.out.println(servico.entrar(json("nome", nome), ouvinte));
        System.out.println("Comandos: publicar <tipo> <mensagem> | historico | status | sair");
        while (entrada.hasNextLine()) {
            String linha = entrada.nextLine().trim();
            if (linha.equalsIgnoreCase("sair")) break;
            if (linha.equalsIgnoreCase("historico")) System.out.println(servico.listarHistorico());
            else if (linha.equalsIgnoreCase("status")) System.out.println(servico.status());
            else if (linha.startsWith("publicar ")) {
                String[] partes = linha.split("\\s+", 3);
                if (partes.length < 3) { System.out.println("Uso: publicar <tipo> <mensagem>"); continue; }
                System.out.println(servico.publicar(json("nome", nome), "{\"tipo\":\"" + escape(partes[1])
                        + "\",\"mensagem\":\"" + escape(partes[2]) + "\"}"));
            } else System.out.println("Comando desconhecido.");
        }
        System.out.println(servico.sair(nome));
        java.rmi.server.UnicastRemoteObject.unexportObject(ouvinte, true);
        entrada.close();
    }
    private static String json(String chave, String valor) { return "{\"" + chave + "\":\"" + escape(valor) + "\"}"; }
    private static String escape(String s) { return s.replace("\\", "\\\\").replace("\"", "\\\""); }
}
