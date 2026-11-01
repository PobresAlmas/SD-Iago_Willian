package rmi.notificacao.model;

import java.io.Serializable;

public abstract class Pessoa implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String nome;

    protected Pessoa(String nome) {
        if (nome == null || nome.trim().isEmpty()) throw new IllegalArgumentException("Nome obrigatorio.");
        this.nome = nome.trim();
    }

    public String getNome() { return nome; }
}
