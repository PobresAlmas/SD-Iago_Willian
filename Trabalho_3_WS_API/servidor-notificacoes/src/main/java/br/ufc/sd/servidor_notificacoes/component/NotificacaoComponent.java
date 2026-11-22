package br.ufc.sd.servidor_notificacoes.component;

import br.ufc.sd.servidor_notificacoes.model.CanalNotificacoes;
import br.ufc.sd.servidor_notificacoes.model.Notificacao;
import br.ufc.sd.servidor_notificacoes.model.Usuario;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class NotificacaoComponent {
    private final List<Notificacao> notificacoes = new CopyOnWriteArrayList<>();
    private final AtomicLong sequenciaId = new AtomicLong(1);
    private final CanalComponent canalComponent;
    private final UsuarioComponent usuarioComponent;

    public NotificacaoComponent(CanalComponent canalComponent, UsuarioComponent usuarioComponent) {
        this.canalComponent = canalComponent;
        this.usuarioComponent = usuarioComponent;
    }

    public Notificacao publicar(Notificacao notificacao) {
        Usuario autor = usuarioComponent.buscarPorId(notificacao.getAdminId());
        if (autor == null || !"ADMIN".equalsIgnoreCase(autor.getTipo())) {
            throw new IllegalArgumentException("Apenas um ADMIN válido pode enviar notificações.");
        }

        CanalNotificacoes canal = canalComponent.buscarPorId(notificacao.getCanalId());
        if (canal == null) {
            throw new IllegalArgumentException("Canal de notificações não encontrado.");
        }

        notificacao.setId(sequenciaId.getAndIncrement());
        notificacao.setDataHora(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
        notificacoes.add(notificacao);
        return notificacao;
    }

    public List<Notificacao> listarPorCliente(Long clienteId) {
        return notificacoes.stream()
                .filter(n -> {
                    CanalNotificacoes canal = canalComponent.buscarPorId(n.getCanalId());
                    return canal != null && canal.getInscritosIds().contains(clienteId);
                })
                .toList();
    }

    public List<Notificacao> listarPorCanal(Long canalId) {
        return notificacoes.stream()
                .filter(n -> n.getCanalId().equals(canalId))
                .toList();
    }
}