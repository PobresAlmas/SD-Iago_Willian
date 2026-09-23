package tcp;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import messages.MessageRequest;
import messages.MessageReply;
import model.Paciente;

public class SerializableClient {
    public static void main(String[] args) {
        try (Socket conexao = new Socket("127.0.0.1", 5000)) {
            
            ObjectOutputStream out = new ObjectOutputStream(conexao.getOutputStream());
            out.flush(); 
            ObjectInputStream in = new ObjectInputStream(conexao.getInputStream());

            Paciente paciente = new Paciente(4, "Shaolin Matador de Porco", 19);
  
            MessageRequest request = new MessageRequest("CADASTRAR_PACIENTE", paciente);

            System.out.println("A enviar pacote de requisição...");
            out.writeObject(request);
            out.flush();

            MessageReply reply = (MessageReply) in.readObject();
            System.out.println("Resposta do Servidor -> Código: " + reply.getStatus() + " | Mensagem: " + reply.getTexto());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}