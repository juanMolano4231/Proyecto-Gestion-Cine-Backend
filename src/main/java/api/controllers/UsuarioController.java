/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package api.controllers;

import api.services.UsuarioService;
import api.models.Usuario;
import api.models.data.UsuarioData;
import api.services.JWTService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Juan José Molano Franco
 */
@RestController
@RequestMapping("/api/usuarios")
@Tag(name = "Usuarios", description = "API para la gestión de usuarios")
public class UsuarioController {

    private final UsuarioService service;
    private final JWTService jwtService;

    @Autowired
    public UsuarioController(PasswordEncoder passwordEncoder, UsuarioService usuarioService) {
        this.jwtService = new JWTService();
        this.service = usuarioService;
    }

    @Transactional
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Usuario usuario) {
        Usuario user = service.login(usuario.getUsuario(), usuario.getPin());

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }

        String jwt = jwtService.generarToken(user);

        Map<String, Object> response = new HashMap<>();
        response.put("token", jwt);
        response.put("usuario", user);

        return ResponseEntity.ok(response);
    }

    @Transactional
    @GetMapping
    public List<Usuario> getAllUsuarios() {
        return service.getAllUsuarios();
    }

    @Transactional
    @GetMapping("/{user}")
    public ResponseEntity<Usuario> getUsuarioPorUser(@PathVariable String user) {
        Usuario u = service.findByUser(user);
        return u != null ? ResponseEntity.ok(u) : ResponseEntity.notFound().build();
    }

//    @Transactional
//    @PutMapping("/{user}")
//    public ResponseEntity<Usuario> updateUsuario(@PathVariable String user, @RequestBody Usuario usuario) {
//        Usuario actualizado = service.updateUsuario(user, usuario);
//        return actualizado != null ? ResponseEntity.ok(actualizado) : ResponseEntity.notFound().build();
//    }
    @Transactional
    @GetMapping("/consultarTipo/{user}")
    public ResponseEntity<String> consultarTipo(@PathVariable String user) {
        String tipo = service.consultarTipo(user);
        return tipo != null ? ResponseEntity.ok(tipo) : ResponseEntity.notFound().build();
    }

}
