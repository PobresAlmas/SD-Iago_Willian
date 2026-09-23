package multicast;

import java.io.*;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServidorMulticast {
    private static final int TCP_PORT = 5001;
    private static final int MULTICAST_PORT = 4446;
    private static final String MULTICAST_IP = "230.0.0.1";

    public static void main(String[] args) {
        ExecutorService threadPool = Executors.newCachedThreadPool();

        threadPool.execute(() -> {
            try (DatagramSocket udpSocket = new DatagramSocket()) {
                InetAddress group = InetAddress.getByName(MULTICAST_IP);
                int contador = 1;
                while (true) {
                    Thread.sleep(10000);

                    String jsonMessage = String.format(
                            "{\n\"tipo\": \"NOTIFICACAO\",\n\"mensagem\": \"Atualizacao %d disponivel\",\n\"timestamp\": %d\n}",
                            contador++, System.currentTimeMillis()
                    );

                    byte[] buffer = jsonMessage.getBytes();
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, MULTICAST_PORT);
                    udpSocket.send(packet);
                    System.out.println("[Servidor UDP] Mensagem multicast disparada.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        try (ServerSocket tcpServer = new ServerSocket(TCP_PORT)) {
            System.out.println("[Servidor TCP] Aguardando autenticação de clientes na porta " + TCP_PORT);
            while (true) {
                Socket clienteSocket = tcpServer.accept();
                threadPool.execute(() -> autenticarCliente(clienteSocket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void autenticarCliente(Socket socket) {
        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
        ) {
            String credencial = in.readLine();
            if (credencial != null && !credencial.trim().isEmpty()) {
                System.out.println("[Servidor TCP] Cliente autenticado: " + credencial);
                out.println("AUTH_SUCCESS:" + MULTICAST_IP + ":" + MULTICAST_PORT);
            } else {
                out.println("AUTH_FAIL");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}