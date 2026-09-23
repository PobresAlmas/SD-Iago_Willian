package votacao;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ClienteEleitor {
    private static final String SERVER_IP = "127.0.0.1";
    private static final int SERVER_TCP_PORT = 6001;

    public static void main(String[] args) {
        String[] authData = conectarTCP("LOGIN_ELEITOR").split("\\|");

        if (authData[0].equals("ENCERRADA")) {
            System.out.println("O prazo para votações foi encerrado.");
            return;
        }

        if (authData[0].equals("OK")) {
            iniciarThreadMulticast(authData[1], Integer.parseInt(authData[2]));
            executarFluxoVotacao();
        }
    }

    private static void executarFluxoVotacao() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\nCandidatos disponiveis:");
        System.out.println(conectarTCP("LISTAR"));

        System.out.print("\nDigite o ID do candidato para votar: ");
        String idVoto = scanner.nextLine();

        String resposta = conectarTCP("VOTAR|" + idVoto);
        System.out.println("Resposta do Servidor: " + resposta);

        System.out.println("Pressione ENTER para sair do sistema (sua thread multicast continuará operando em background).");
        scanner.nextLine();
        scanner.close();
        System.exit(0);
    }

    private static String conectarTCP(String requisicao) {
        try (Socket socket = new Socket(SERVER_IP, SERVER_TCP_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            out.println(requisicao);
            return in.readLine();
        } catch (IOException e) {
            return "ERRO_CONEXAO";
        }
    }

    private static void iniciarThreadMulticast(String ip, int port) {
        Thread thread = new Thread(() -> {
            try (MulticastSocket mSocket = new MulticastSocket(port)) {
                InetAddress group = InetAddress.getByName(ip);
                mSocket.joinGroup(group);

                byte[] buffer = new byte[2048];
                while (true) {
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    mSocket.receive(packet);
                    String msg = new String(packet.getData(), 0, packet.getLength());
                    System.out.println("\n[AVISO DA ADMINISTRACAO] " + msg);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        thread.setDaemon(true);
        thread.start();
    }
}