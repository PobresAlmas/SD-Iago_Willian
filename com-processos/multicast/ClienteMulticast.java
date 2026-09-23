package multicast;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ClienteMulticast {
    private static final String SERVER_IP = "127.0.0.1";
    private static final int SERVER_TCP_PORT = 5001;
    private static MulticastSocket multicastSocket;
    private static InetAddress group;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Digite seu nome de usuário para autenticação (TCP): ");
        String username = scanner.nextLine();

        if (autenticarTCP(username)) {
            iniciarThreadMulticast();

            // Thread principal atua como interface de usuário
            while (true) {
                System.out.println("\nComandos: [sair]");
                String comando = scanner.nextLine();
                if (comando.equalsIgnoreCase("sair")) {
                    desconectarMulticast();
                    break;
                }
            }
        } else {
            System.err.println("Falha na autenticação.");
        }
        scanner.close();
    }

    private static boolean autenticarTCP(String username) {
        try (Socket socket = new Socket(SERVER_IP, SERVER_TCP_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            out.println(username);
            String resposta = in.readLine();

            if (resposta != null && resposta.startsWith("AUTH_SUCCESS")) {
                String[] partes = resposta.split(":");
                group = InetAddress.getByName(partes[1]);
                multicastSocket = new MulticastSocket(Integer.parseInt(partes[2]));
                System.out.println("Autenticação TCP bem-sucedida. Endereço Multicast recebido.");
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static void iniciarThreadMulticast() {
        Thread listener = new Thread(() -> {
            try {
                multicastSocket.joinGroup(group);
                System.out.println("Inscrito no grupo Multicast (" + group.getHostAddress() + "). Aguardando dados...\n");

                byte[] buffer = new byte[1024];
                while (!multicastSocket.isClosed()) {
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    multicastSocket.receive(packet);
                    String mensagem = new String(packet.getData(), 0, packet.getLength());
                    System.out.println("\n[Multicast Recebido] -> \n" + mensagem);
                }
            } catch (SocketException e) {
                System.out.println("Socket Multicast encerrado.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        listener.setDaemon(true);
        listener.start();
    }

    private static void desconectarMulticast() {
        try {
            if (multicastSocket != null && !multicastSocket.isClosed()) {
                multicastSocket.leaveGroup(group);
                multicastSocket.close();
                System.out.println("Desconectado do grupo Multicast.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}