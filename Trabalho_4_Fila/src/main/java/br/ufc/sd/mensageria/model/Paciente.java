package br.ufc.sd.mensageria.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class Paciente extends Pessoa {
    private String nome;
    private int idade;

    @Override
    public String toString() {
        return "Paciente {" +
                "id=" + getId() +
                ", nome='" + nome + '\'' +
                ", idade=" + idade +
                " }";
    }
}