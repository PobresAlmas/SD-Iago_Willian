package br.ufc.sd.servidor_notificacoes.component;

import br.ufc.sd.servidor_notificacoes.model.CanalNotificacoes;
import br.ufc.sd.servidor_notificacoes.model.Usuario;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class CanalComponent {
    private final Map<Long, CanalNotificacoes> canais = new ConcurrentHashMap<>();
    private final AtomicLong sequenciaId = new AtomicLong(1);
    private final UsuarioComponent usuarioComponent;

    public CanalComponent(UsuarioComponent usuarioComponent) {
        this.usuarioComponent = usuarioComponent;
    }

    public CanalNotificacoes criarCanal(CanalNotificacoes canal) {
        Usuario criador = usuarioComponent.buscarPorId(canal.getAdminId());
        if (criador == null || !"ADMIN".equalsIgnoreCase(criador.getTipo())) {
            throw new IllegalArgumentException("Apenas um ADMIN válido pode criar canais.");
        }
        canal.setId(sequenciaId.getAndIncrement());
        canais.put(canal.getId(), canal);
        return canal;
    }

    public boolean inscreverCliente(Long canalId, Long clienteId) {
        CanalNotificacoes canal = canais.get(canalId);
        Usuario usuario = usuarioComponent.buscarPorId(clienteId);
        if (canal == null || usuario == null) {
            return false;
        }
        canal.adicionarInscrito(clienteId);
        return true;
    }

    public boolean cancelarInscricao(Long canalId, Long clienteId) {
        CanalNotificacoes canal = canais.get(canalId);
        if (canal == null) {
            return false;
        }
        canal.removerInscrito(clienteId);
        return true;
    }

    public CanalNotificacoes buscarPorId(Long id) {
        return canais.get(id);
    }

    public Collection<CanalNotificacoes> listarTodos() {
        return canais.values();
    }
}