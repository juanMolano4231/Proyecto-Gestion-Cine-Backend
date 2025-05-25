/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package api.controllers;

import api.models.LoginResponse;
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
import org.springframework.web.bind.annotation.RequestHeader;
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
    public UsuarioController(UsuarioService usuarioService, JWTService jwtService) {
        this.jwtService = jwtService;
        this.service = usuarioService;
    }

    @Transactional
    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión", description = "Valida las credenciales del usuario y devuelve un token JWT si son correctas.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Inicio de sesión exitoso"),
        @ApiResponse(responseCode = "401", description = "Credenciales inválidas")
    })
    public ResponseEntity<LoginResponse> login(@RequestBody @Parameter(description = "Credenciales del usuario (usuario y pin)") Usuario usuario) {
        Usuario user = service.login(usuario.getUsuario(), usuario.getPin());

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        String tipo = service.consultarTipo(user.getUsuario());

        String jwt = jwtService.generarToken(user, tipo);

        LoginResponse response = new LoginResponse();
        response.setToken(jwt);
        response.setUsuario(user);

        return ResponseEntity.ok(response);
    }

    @Transactional
    @GetMapping("/consultarTipo/{user}")
    @Operation(summary = "Consultar tipo de usuario", description = "Devuelve el tipo de rol asignado al usuario (admin, empleado, etc).")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tipo de usuario encontrado"),
        @ApiResponse(responseCode = "401", description = "Token JWT inválido o ausente"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    public ResponseEntity<String> consultarTipo(
            @PathVariable @Parameter(description = "Nombre de usuario") String user,
            @RequestHeader(value = "Authorization", required = false)
            @Parameter(description = "Token JWT en el encabezado Authorization (formato: Bearer <token>)") String authHeader) {
        String token = this.jwtService.extractToken(authHeader);
        if (token == null || !this.jwtService.validarToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or missing JWT token");
        }

        String tipo = service.consultarTipo(user);
        return tipo != null ? ResponseEntity.ok(tipo) : ResponseEntity.notFound().build();
    }

    @GetMapping("/checkUsername/{user}")
    @Operation(summary = "Verificar disponibilidad de nombre de usuario", description = "Indica si un nombre de usuario ya está en uso.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Consulta realizada exitosamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Boolean> checkUsername(
            @PathVariable @Parameter(description = "Nombre de usuario a verificar") String user) {
        Boolean disponible = service.checkUsername(user);
        return disponible != null ? ResponseEntity.ok(disponible) : ResponseEntity.internalServerError().build();
    }

}
