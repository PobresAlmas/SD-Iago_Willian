package streams;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import model.Paciente;

public class PacienteInputStream extends InputStream {
    private ArrayList<Paciente> pacientes;
    private DataInputStream dts;

    public PacienteInputStream(InputStream in) {
        dts = new DataInputStream(in);
        pacientes = new ArrayList<>();
    }

    public void recoveryData() {
        try {
            while (true) {
                int id = dts.readInt();
                int idade = dts.readInt();
                String nome = dts.readUTF();

                Paciente paciente = new Paciente(id, nome, idade);
                pacientes.add(paciente);
            }

        } catch (EOFException e){
            System.out.println("Finalizado");
            System.out.println(pacientes);
        } 
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int read() throws IOException {
        return dts.read();
    }

}
