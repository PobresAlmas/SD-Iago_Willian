package br.ufc.sd.servidor_notificacoes.controller;

import br.ufc.sd.servidor_notificacoes.component.CanalComponent;
import br.ufc.sd.servidor_notificacoes.model.CanalNotificacoes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Map;

@RestController
@RequestMapping("/api/canais")
public class CanalController {

    private final CanalComponent canalComponent;

    public CanalController(CanalComponent canalComponent) {
        this.canalComponent = canalComponent;
    }

    @PostMapping
    public ResponseEntity<?> criarCanal(@RequestBody CanalNotificacoes canal) {
        try {
            CanalNotificacoes criado = canalComponent.criarCanal(canal);
            return ResponseEntity.status(HttpStatus.CREATED).body(criado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<Collection<CanalNotificacoes>> listarTodos() {
        return ResponseEntity.ok(canalComponent.listarTodos());
    }

    @PostMapping("/{canalId}/inscrever/{clienteId}")
    public ResponseEntity<?> inscrever(@PathVariable Long canalId, @PathVariable Long clienteId) {
        boolean sucesso = canalComponent.inscreverCliente(canalId, clienteId);
        if (!sucesso) {
            return ResponseEntity.badRequest().body(Map.of("erro", "Canal ou Cliente não encontrado."));
        }
        return ResponseEntity.ok(Map.of("mensagem", "Inscrição realizada com sucesso."));
    }

    @DeleteMapping("/{canalId}/inscrever/{clienteId}")
    public ResponseEntity<?> cancelarInscricao(@PathVariable Long canalId, @PathVariable Long clienteId) {
        boolean sucesso = canalComponent.cancelarInscricao(canalId, clienteId);
        if (!sucesso) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(Map.of("mensagem", "Inscrição cancelada."));
    }
}