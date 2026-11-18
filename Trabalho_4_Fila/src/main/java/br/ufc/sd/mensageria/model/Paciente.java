package br.ufc.sd.mensageria.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;


@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class Paciente implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final UUID id = UUID.randomUUID();
    private String nome;
    private int idade;

    @Override
    public String toString() {
        return "Paciente {" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", idade=" + idade +
                " }";
    }
}