package br.ufc.sd.mensageria.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class Prontuario implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final UUID id = UUID.randomUUID();
    private final UUID pacienteId;
    private String remedios;
}
