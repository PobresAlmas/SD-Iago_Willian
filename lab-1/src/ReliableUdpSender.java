import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.Random;

public class ReliableUdpSender {
    private int numberSeq;
    private int port;
    private static final double LOSS_RATE = 0.3;
    private InetAddress host;
    private DatagramSocket socketSend;
    private Random random;

    public ReliableUdpSender(int port, String host) throws Exception {
        this.socketSend = new DatagramSocket();
        this.port = port;
        this.host = InetAddress.getByName(host);
        this.socketSend.setSoTimeout(1000);
        this.random = new Random();
    }

    public boolean send(String payload) throws Exception{
        int count = 0, max = 10;
        boolean ackReceived = false;
        byte[] bufferSend = new byte[1024];
        byte[] bufferReceive = new byte[1024];

        bufferSend = String.format("SEQ:<%d>:<%s>", numberSeq, payload).getBytes();
        DatagramPacket packetSend = new DatagramPacket(bufferSend, bufferSend.length, host, port);

        while (count < max && !ackReceived){
            if (random.nextDouble() < LOSS_RATE){
                System.out.println("Reply not sent.");
            } else {
                socketSend.send(packetSend);
            }

            try {
                DatagramPacket packetReceive = new DatagramPacket(bufferReceive, bufferReceive.length);
                socketSend.receive(packetReceive);
                String mssReceive = new String(packetReceive.getData(), 0, packetReceive.getLength());

                int ack = Integer.parseInt(mssReceive.split(":")[1].replace("<", "").replace(">", "").trim());

                if (ack == numberSeq) {
                    ackReceived = true;

                    if (numberSeq == 0) numberSeq = 1;
                    else numberSeq = 0;
                }

            } catch (SocketTimeoutException e) {
                count ++;
            }
        }

        return ackReceived;
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.out.println("Required arguments: host port");
            return;
        }

        String host = args[0];
        int port = Integer.parseInt(args[1]);

        System.out.println("Inicializando o remetente UDP confiável...");
       
        ReliableUdpSender sender = new ReliableUdpSender(port, host);

        String payloadTeste = "Mensagem de teste";

        System.out.println("Tentando enviar a mensagem: " + payloadTeste);
        boolean sucesso = sender.send(payloadTeste);

        if (sucesso) {
            System.out.println("Resultado: SUCESSO! O pacote foi entregue e o ACK foi recebido.");
        } else {
            System.out.println("Resultado: FALHA. O limite de 10 retransmissões foi atingido sem resposta.");
        }
    }
}
