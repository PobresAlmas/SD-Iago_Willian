package streams;

import model.Prontuario;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class ProntuarioInputStream extends InputStream {
    private final ArrayList<Prontuario> protuario;
    private final DataInputStream dts;

    public ProntuarioInputStream(InputStream in) {
        dts = new DataInputStream(in);
        protuario = new ArrayList<>();
    }

    public void recoveryData() {
        try {
            while (true) {
                int id = dts.readInt();
                int pacienteId = dts.readInt();
                String remedios = dts.readUTF();

                Prontuario prontuario = new Prontuario(id, pacienteId, remedios);
                protuario.add(prontuario);
            }

        } catch (EOFException e) {
            System.out.println("Finalizado");
            System.out.println(protuario);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int read() throws IOException {
        return dts.read();
    }

}
