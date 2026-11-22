package br.ufc.sd.servidor_notificacoes.controller;

import br.ufc.sd.servidor_notificacoes.component.UsuarioComponent;
import br.ufc.sd.servidor_notificacoes.model.Usuario;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioComponent usuarioComponent;

    public UsuarioController(UsuarioComponent usuarioComponent) {
        this.usuarioComponent = usuarioComponent;
    }

    @PostMapping
    public ResponseEntity<Usuario> cadastrar(@RequestBody Usuario usuario) {
        Usuario criado = usuarioComponent.cadastrar(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(criado);
    }

    @GetMapping
    public ResponseEntity<Collection<Usuario>> listarTodos() {
        return ResponseEntity.ok(usuarioComponent.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarPorId(@PathVariable Long id) {
        Usuario usuario = usuarioComponent.buscarPorId(id);
        if (usuario == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(usuario);
    }
}