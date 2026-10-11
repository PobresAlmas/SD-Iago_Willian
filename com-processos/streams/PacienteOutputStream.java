package streams;

import model.Paciente;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class PacienteOutputStream extends OutputStream {

    private final Paciente[] pacientes;
    private final DataOutputStream data;
    private final int qtdPacientes;

    public PacienteOutputStream(Paciente[] pacientes, OutputStream fluxo, int qtdPacientes) {
        this.pacientes = pacientes;
        this.data = new DataOutputStream(fluxo);
        this.qtdPacientes = qtdPacientes;
    }

    public void enviarPacientes() {
        try {
            for (int i = 0; i < qtdPacientes; i++) {
                data.writeInt(pacientes[i].getId());
                data.writeInt(pacientes[i].getIdade());
                data.writeUTF(pacientes[i].getNome());
            }

            data.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void write(int b) throws IOException {
        data.write(b);
    }

}
