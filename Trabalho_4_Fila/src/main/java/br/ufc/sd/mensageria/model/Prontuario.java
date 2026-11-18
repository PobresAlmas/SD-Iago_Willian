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
public class Prontuario implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final UUID id = UUID.randomUUID();
    private final UUID pacienteId;
    private String remedios;

    @Override
    public String toString() {
        return "Prontuario {" +
                "id=" + id +
                ", pacienteId=" + pacienteId +
                ", remedios='" + remedios + '\'' +
                " }";
    }
}
