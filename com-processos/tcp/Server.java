package tcp;

import streams.PacienteInputStream;

import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static void outputServer(InputStream in) {
        try {
            int b;
            while ((b = in.read()) != -1) {
                System.out.println((char) b);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void inputServer(InputStream in) {
        try (PacienteInputStream pis = new PacienteInputStream(in)) {
            pis.recoveryData();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void main(String[] args) {
        try (ServerSocket server = new ServerSocket(5000)) {

            Socket conexao = server.accept();
            InputStream in = conexao.getInputStream();

            // outputServer(in);
            inputServer(in);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
