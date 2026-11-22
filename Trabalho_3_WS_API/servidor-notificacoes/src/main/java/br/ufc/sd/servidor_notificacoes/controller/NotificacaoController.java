package br.ufc.sd.servidor_notificacoes.controller;

import br.ufc.sd.servidor_notificacoes.component.NotificacaoComponent;
import br.ufc.sd.servidor_notificacoes.model.Notificacao;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notificacoes")
public class NotificacaoController {

    private final NotificacaoComponent notificacaoComponent;

    public NotificacaoController(NotificacaoComponent notificacaoComponent) {
        this.notificacaoComponent = notificacaoComponent;
    }

    @PostMapping
    public ResponseEntity<?> publicar(@RequestBody Notificacao notificacao) {
        try {
            Notificacao criada = notificacaoComponent.publicar(notificacao);
            return ResponseEntity.status(HttpStatus.CREATED).body(criada);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        }
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<Notificacao>> listarPorCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(notificacaoComponent.listarPorCliente(clienteId));
    }

    @GetMapping("/canal/{canalId}")
    public ResponseEntity<List<Notificacao>> listarPorCanal(@PathVariable Long canalId) {
        return ResponseEntity.ok(notificacaoComponent.listarPorCanal(canalId));
    }
}