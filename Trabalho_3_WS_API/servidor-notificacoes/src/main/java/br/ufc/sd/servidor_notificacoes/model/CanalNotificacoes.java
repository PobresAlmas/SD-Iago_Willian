package br.ufc.sd.servidor_notificacoes.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Data
@NoArgsConstructor
public class CanalNotificacoes {
    private Long id;
    private String nome;
    private String descricao;
    private Long adminId;
    private Set<Long> inscritosIds = ConcurrentHashMap.newKeySet();

    public void adicionarInscrito(Long clienteId) {
        this.inscritosIds.add(clienteId);
    }

    public void removerInscrito(Long clienteId) {
        this.inscritosIds.remove(clienteId);
    }
}