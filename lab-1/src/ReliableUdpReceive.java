import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;    

/*
 * Servidor para processar as requisições de Ping sobre UDP.
 */
public class ReliableUdpReceive {

    public static void main(String[] args) throws Exception {

        System.out.println("Servidor Confiável iniciado");

        // Obter o argumento da linha de comando.
        if (args.length != 1) {
            System.out.println("Required arguments: port");
            return;
        }

        // Gerador de números aleatórios p/ simular perda de pacotes e atrasos na rede.
        byte[] buffer = new byte[1024];

        int port = Integer.parseInt(args[0]);
        int expectedSeqNum = 0;

        try (DatagramSocket socket = new DatagramSocket(port)) {
            while (true) {
                // Criar um pacote de datagrama para comportar o pacote UDP de chegada.
                DatagramPacket request = new DatagramPacket(buffer, buffer.length);
                // Bloquear até que o hospedeiro receba o pacote UDP.
                socket.receive(request);
                // Imprimir os dados recebidos.

                String[] partes = new String(request.getData(), 0, request.getLength()).split(":", 3);

                if (partes.length != 3 || !partes[0].equals("SEQ")) {
                    System.out.println("Invalid packet");
                    continue;
                }

                int seqNum = Integer.parseInt(partes[1].replace("<", "").replace(">", "").trim());

                printData(request);

                // Decidir se responde, ou simula perda de pacotes.
                if (seqNum == expectedSeqNum) {
                    System.out.println("ACK Received");
                    sendAck(socket, request.getAddress(), request.getPort(), seqNum);
                    // Alterna o esperado entre 0 e 1
                    expectedSeqNum = (expectedSeqNum == 0) ? 1 : 0;

                } else if (seqNum < expectedSeqNum) {
                    System.out.println("ACK Out of order / Duplicado");
                    // Reenvia o ACK do que já foi processado para destravar o remetente
                    sendAck(socket, request.getAddress(), request.getPort(), seqNum);
                    
                } else {
                    System.out.println("ACK Fora do limite / Futuro inesperado");
                    sendAck(socket, request.getAddress(), request.getPort(), expectedSeqNum == 0 ? 1 : 0);
                }
            }
        }
    }

    /*
     * Imprimir o dado de Ping para o trecho de saída padrão.
     */
    private static void printData(DatagramPacket request)

            throws Exception {
        // Obter referências para a ordem de pacotes de bytes.
        byte[] buf = request.getData();
        // Envolver os bytes numa cadeia de entrada vetor de bytes, de modo que você possa ler os dados como uma cadeia de bytes.
        BufferedReader br = bytesToBufferedReader(buf);
        // O dado da mensagem está contido numa única linha, então leia esta linha.
        String line = br.readLine();
        // Imprimir o endereço do hospedeiro e o dado recebido dele.
        System.out.println("Received from " + request.getAddress().getHostAddress() + ":" + line);
    }

    private static BufferedReader bytesToBufferedReader(byte[] buf) {
        ByteArrayInputStream bais = new ByteArrayInputStream(buf);
        // Envolver a cadeia de saída do vetor bytes num leitor de cadeia de
        // entrada, de modo que você possa ler os dados como uma cadeia de caracteres.
        InputStreamReader isr = new InputStreamReader(bais);
        // Envolver o leitor de cadeia de entrada num leitor com armazenagem, de
        // modo que você possa ler os dados de caracteres linha a linha. (A linha é uma seqüência de caracteres terminados por alguma combinação de \r e \n.)
        BufferedReader br = new BufferedReader(isr);
        return br;
    }

    private static void sendAck(DatagramSocket socket, InetAddress inetAddress, int port, int seqNUm) throws Exception {
        String data = "ACK:" + seqNUm;

        byte[] buffer = data.getBytes();

        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, inetAddress, port);
        socket.send(packet);

        System.out.println("  <- Resposta enviada: " + data);
    }
}