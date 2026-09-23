package model;

import java.io.Serial;
import java.io.Serializable;

public class Paciente implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final String nome;
    private final int id;
    private int idade;

    public Paciente(int id, String nome, int idade){
        this.id = id;
        this.idade = idade;
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public int getId() {
        return id;
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    @Override
    public String toString() {
        return "Paciente [nome=" + nome + ", id=" + id + ", idade=" + idade + "]";
    }
}