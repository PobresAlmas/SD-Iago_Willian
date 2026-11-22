package br.ufc.sd.servidor_notificacoes.component;

import br.ufc.sd.servidor_notificacoes.model.Usuario;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class UsuarioComponent {
    private final Map<Long, Usuario> usuarios = new ConcurrentHashMap<>();
    private final AtomicLong sequenciaId = new AtomicLong(1);

    public Usuario cadastrar(Usuario usuario) {
        usuario.setId(sequenciaId.getAndIncrement());
        if (usuario.getTipo() == null || usuario.getTipo().isBlank()) {
            usuario.setTipo("CLIENTE");
        } else {
            usuario.setTipo(usuario.getTipo().toUpperCase());
        }
        usuarios.put(usuario.getId(), usuario);
        return usuario;
    }

    public Usuario buscarPorId(Long id) {
        return usuarios.get(id);
    }

    public Collection<Usuario> listarTodos() {
        return usuarios.values();
    }
}