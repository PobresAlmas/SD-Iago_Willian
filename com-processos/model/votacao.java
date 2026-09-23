package votacao;

import java.io.Serializable;

public class Candidato implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String nome;
    private int votos;

    public Candidato(int id, String nome) {
        this.id = id;
        this.nome = nome;
        this.votos = 0;
    }

    public synchronized void incrementarVoto() {
        this.votos++;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public int getVotos() {
        return votos;
    }

    public String toJson() {
        return String.format("{\"id\": %d, \"nome\": \"%s\", \"votos\": %d}", id, nome, votos);
    }
}