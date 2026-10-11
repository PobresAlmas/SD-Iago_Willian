package streams;

import model.Prontuario;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ProntuarioOutputStream extends OutputStream {
    private final Prontuario[] prontuarios;
    private final DataOutputStream data;
    private final int qtdProntuarios;

    public ProntuarioOutputStream(Prontuario[] prontuarios, OutputStream fluxo, int qtdProntuarios) {
        this.prontuarios = prontuarios;
        this.data = new DataOutputStream(fluxo);
        this.qtdProntuarios = qtdProntuarios;
    }

    public void enviarProntuarios() {
        try {
            for (int i = 0; i < qtdProntuarios; i++) {
                data.writeInt(prontuarios[i].getId());
                data.writeInt(prontuarios[i].getPacienteId());
                data.writeUTF(prontuarios[i].getRemedios());
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
