import java.net.*;
import java.util.*;

/*
* Cliente para realizar requisições de Ping sobre UDP.
*/
public class PingClient {
    private static DatagramSocket socketClient;
    private static int packetLose = 0;

    public static void main(String[] args) throws Exception {

        // Obter o argumento da linha de comando.
        if (args.length != 2) {
            System.out.println("Required arguments: host, port");
            return;
        }

        byte[] bufferSend = new byte[1024];
        byte[] bufferReceive = new byte[1024];
        String host = args[0];
        int port = Integer.parseInt(args[1]);
        
        socketClient = new DatagramSocket();
        InetAddress IPAddress = InetAddress.getByName(host);
        
        for (int i = 1; i <= 10; i++){
            try {
                Date dt = new Date();
                bufferSend = String.format("PING %d %s \r\n", i, dt).getBytes();
                // Datagrama a ser enviado
                DatagramPacket packetSend = new DatagramPacket(bufferSend, bufferSend.length, IPAddress, port);
                // Envia
                socketClient.send(packetSend);
                
                socketClient.setSoTimeout(1000);
                DatagramPacket packetReceive = new DatagramPacket(bufferReceive, bufferReceive.length);
                socketClient.receive(packetReceive);
                
                String mssReceive = new String(packetReceive.getData(), 0, packetReceive.getLength());
                
                System.out.println("Olha só o que recebemos de volta " + mssReceive);
                
            } catch (SocketTimeoutException e) {
                packetLose++;
                System.out.printf("Pacote perdido: %d\n", i);
            }
        }
        
        socketClient.close();
        System.out.printf("\n Número de pacotes perdidos: %d \n", packetLose);
        
    }
}