package model;

import java.io.Serial;
import java.io.Serializable;

public class Prontuario implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final int id;
    private final int pacienteId;
    private String remedios;

    public Prontuario(int id, int pacienteId, String remedios) {
        this.id = id;
        this.pacienteId = pacienteId;
        this.remedios = remedios;
    }

    public int getId() {
        return id;
    }

    public String getRemedios() {
        return remedios;
    }

    public int getPacienteId() {
        return pacienteId;
    }

    public void setRemedios(String remedios) {
        this.remedios = remedios;
    }

}
