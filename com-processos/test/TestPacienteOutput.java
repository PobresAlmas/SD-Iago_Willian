package test;

// import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.Socket;

import model.Paciente;
import streams.PacienteOutputStream;

public class TestPacienteOutput {

    private static Paciente[] pacientes = {
            new Paciente(0, "Gugu", 45),
            new Paciente(1, "Grande Homem", 94),
            new Paciente(2, "Gloria", 89)};

    public static void test(OutputStream fluxo){
        try (PacienteOutputStream testPaciente = new PacienteOutputStream(pacientes, fluxo, pacientes.length)) {
            testPaciente.enviarPacientes();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[]){
        // try {
        //     test(System.out);
        //     test(new FileOutputStream("pacientes.bin"));
        // } catch (Exception e) {
        //     e.printStackTrace();
        // }

        try (Socket socket = new Socket("127.0.0.1", 5000)){
            test(socket.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
