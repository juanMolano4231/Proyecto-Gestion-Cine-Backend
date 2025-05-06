/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package api.controllers;

import api.services.UsuarioService;
import api.models.Usuario;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Juan José Molano Franco
 */
@RestController
@RequestMapping("/api/usuarios_data")
@Tag(name = "Usuarios", description = "API para la gestión de usuarios")
public class UsuarioController {

    private final UsuarioService service;

    @Autowired
    public UsuarioController(UsuarioService service) {
        this.service = service;
        System.out.println(">>> UsuarioController inicializado");
    }

    @Transactional
    @GetMapping
    public List<Usuario> getAllUsuarios() {
        return service.getAllUsuarios();
    }

    @Transactional
    @PostMapping
    public Usuario createUsuario(@RequestBody @Parameter(description = "Datos del usuario a crear") Usuario usuario) {
        return service.saveUsuario(usuario);
    }

    @Transactional
    @GetMapping("/{user}")
    public ResponseEntity<Usuario> getUsuarioPorUser(@PathVariable String user) {
        Usuario u = service.findByUser(user);
        return u != null ? ResponseEntity.ok(u) : ResponseEntity.notFound().build();
    }

    @Transactional
    @PutMapping("/{user}")
    public ResponseEntity<Usuario> updateUsuario(@PathVariable String user, @RequestBody Usuario usuario) {
        Usuario actualizado = service.updateUsuario(user, usuario);
        return actualizado != null ? ResponseEntity.ok(actualizado) : ResponseEntity.notFound().build();
    }

}
