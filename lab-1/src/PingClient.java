import java.io.IOException;
import java.net.*;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/*
 * Cliente para realizar requisições de Ping sobre UDP.
 */
public class PingClient {
    private static int packetLoss = 0;
    private static int countPacket = 0;
    private static int countRecevied = 0;
    private static long minRtt = Long.MAX_VALUE;
    private static long maxRtt = Long.MIN_VALUE;
    private static long sumRtt = 0;

    private static final int MAX_PACKETS = 10;

    public static void main(String[] args) throws Exception {

        // Obter o argumento da linha de comando.
        if (args.length != 2) {
            System.out.println("Required arguments: host, port");
            return;
        }

        String host = args[0];
        int port = Integer.parseInt(args[1]);

        DatagramSocket socketClient = new DatagramSocket();
        socketClient.setSoTimeout(1000);

        InetAddress IPAddress = InetAddress.getByName(host);

        Timer timer = new Timer();

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (countPacket >= MAX_PACKETS) {
                    System.out.println("\n--- Estatísticas do Ping ---");
                    System.out.printf("%d pacotes transmitidos, %d recebidos, %d perdidos (%.1f%% de perda)%n",
                            MAX_PACKETS, countRecevied, packetLoss, (packetLoss / (double) MAX_PACKETS) * 100);

                    if (countRecevied > 0) {
                        double avgRtt = sumRtt / (double) countRecevied;
                        System.out.printf("RTT mínimo/médio/máximo = %d/%.1f/%d ms%n", minRtt, avgRtt, maxRtt);
                    } else {
                        System.out.println("Nenhum pacote recebido — não foi possível calcular RTT.");
                    }

                    timer.cancel();
                    socketClient.close();
                    return;
                }

                byte[] bufferSend;
                byte[] bufferReceive = new byte[1024];

                try {
                    Date dt = new Date();

                    bufferSend = String.format("PING %d %s \r\n", countPacket++, dt).getBytes();
                    // Datagrama a ser enviado

                    DatagramPacket packetSend = new DatagramPacket(bufferSend, bufferSend.length, IPAddress, port);
                    // Envia

                    long startTime = System.currentTimeMillis();
                    socketClient.send(packetSend);

                    DatagramPacket packetReceive = new DatagramPacket(bufferReceive, bufferReceive.length);
                    socketClient.receive(packetReceive);

                    long rtt = System.currentTimeMillis() - startTime;

                    String mssReceive = new String(packetReceive.getData(), 0, packetReceive.getLength());
                    System.out.printf("Olha só o que recebemos de volta %s do pacote %d%n\n", mssReceive, countPacket);

                    countRecevied++;
                    minRtt = Math.min(rtt, minRtt);
                    maxRtt = Math.max(rtt, maxRtt);
                    sumRtt += rtt;


                } catch (SocketTimeoutException | SocketException e) {
                    packetLoss++;
                    System.out.printf("Pacote perdido: %d\n", countPacket);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }, 0, 1000);
    }
}