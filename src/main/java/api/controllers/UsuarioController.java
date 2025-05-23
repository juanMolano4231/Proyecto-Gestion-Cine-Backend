/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package api.controllers;

import api.services.UsuarioService;
import api.models.Usuario;
import api.models.data.UsuarioData;
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

    @Autowired
    public UsuarioController(PasswordEncoder passwordEncoder, UsuarioService usuarioService) {
        this.service = usuarioService;
    }

    @Transactional
    @PostMapping("/login")
    @Operation(summary = "Iniciar sesion", description = "Se provee el nombre de usuario y el pin para realizar el inicio de sesion")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Inicio de sesion exitoso"),
        @ApiResponse(responseCode = "400", description = "Datos incorrectos o invalidos")
    })
    public ResponseEntity<Usuario> login(@RequestBody Usuario usuario) {
        Usuario user = service.login(usuario.getUsuario(), usuario.getPin());
        return user == null ? ResponseEntity.status(HttpStatus.UNAUTHORIZED).build() : ResponseEntity.ok(user);
    }

    @Transactional
    @GetMapping
    @Operation(summary = "Obtener todos los usuarios", description = "Obtiene una lista con todos los usuarios")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista obtenida con exito"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public List<Usuario> getAllUsuarios() {
        return service.getAllUsuarios();
    }

    @Transactional
    @GetMapping("/{user}")
    @Operation(summary = "Obtener por usuario", description = "Obtiene un usuario en base a su nombre de usuario")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario obtenido con exito"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    public ResponseEntity<Usuario> getUsuarioPorUser(@PathVariable String user) {
        Usuario u = service.findByUser(user);
        return u != null ? ResponseEntity.ok(u) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Obtener por usuario", description = "Obtiene un usuario en base a su nombre de usuario")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario obtenido con exito"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @Transactional
    @GetMapping("/consultarTipo/{user}")
    public ResponseEntity<String> consultarTipo(@PathVariable String user) {
        String tipo = service.consultarTipo(user);
        return tipo != null ? ResponseEntity.ok(tipo) : ResponseEntity.notFound().build();
    }
}
