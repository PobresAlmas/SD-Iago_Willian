package tcp;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import messages.MessageRequest;
import messages.MessageReply;

public class SerializableServer {
    public static void main(String[] args) {
        try (ServerSocket server = new ServerSocket(5000)) {

            System.out.println("Servidor escuta na porta 5000...");
            Socket conexao = server.accept();
            System.out.println("Cliente conectado!");

            ObjectOutputStream out = new ObjectOutputStream(conexao.getOutputStream());
            out.flush();
            ObjectInputStream in = new ObjectInputStream(conexao.getInputStream());

            MessageRequest request = (MessageRequest) in.readObject();
            System.out.println("Requisição recebida: " + request.getOperation());

            System.out.println("Dados recebidos: " + request.getObject().toString());

            MessageReply reply = new MessageReply(200, "A operação " + request.getOperation() + " foi processada com sucesso no servidor.");
            out.writeObject(reply);
            out.flush();
            System.out.println("Resposta enviada de volta ao cliente.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}