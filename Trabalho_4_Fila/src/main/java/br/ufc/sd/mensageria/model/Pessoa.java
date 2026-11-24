package br.ufc.sd.mensageria.model;

import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@Getter
public class Pessoa implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final UUID id = UUID.randomUUID();
}
