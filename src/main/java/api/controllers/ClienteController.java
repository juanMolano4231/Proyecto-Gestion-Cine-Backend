/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package api.controllers;

import api.models.Cliente;
import api.repositories.SalaRepository;
import api.services.ClienteService;
import api.services.JWTService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author johan
 */
@RestController
@RequestMapping("/api/clientes")
@Tag(name = "Clientes", description = "API para la gestión de clientes")
public class ClienteController {

    private final ClienteService service;
    private final JWTService jwtService;
    private static final Logger logger = LoggerFactory.getLogger(SalaRepository.class);

    @Autowired
    public ClienteController(ClienteService service, JWTService jwtService) {
        this.jwtService = jwtService;
        this.service = service;

    }

    @Transactional
    @PostMapping("/{user}")
    @Operation(summary = "Actualizar cliente existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cliente actualizado con éxito"),
        @ApiResponse(responseCode = "401", description = "Token inválido o usuario no autorizado"),
        @ApiResponse(responseCode = "404", description = "Cliente no encontrado")
    })
    public ResponseEntity<Cliente> postCliente(
            @PathVariable @Parameter(description = "Nombre de usuario del cliente") String user,
            @RequestBody @Parameter(description = "Datos del cliente a actualizar") Cliente cliente,
            @RequestHeader(value = "Authorization", required = false)
            @Parameter(description = "Token JWT en el encabezado Authorization (formato: Bearer <token>)") String authHeader) {

        String token = this.jwtService.extractToken(authHeader);
        if (token == null || !this.jwtService.validarToken(token) || !this.jwtService.obtenerUsuario(token).equals(user)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Cliente actualizado = service.updateCliente(user, cliente);
        if (actualizado == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(actualizado);
    }

    @Transactional
    @PostMapping
    @Operation(summary = "Crear nuevo cliente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cliente creado con éxito"),
        @ApiResponse(responseCode = "400", description = "Datos del cliente inválidos")
    })
    public ResponseEntity<Cliente> createCliente(
            @RequestBody @Parameter(description = "Datos del cliente a crear") Cliente cliente) {

        Cliente c = service.saveCliente(cliente);
        return c == null ? ResponseEntity.badRequest().build() : ResponseEntity.ok(cliente);
    }

    @Transactional
    @GetMapping("/{user}")
    @Operation(summary = "Obtener cliente por nombre de usuario")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cliente encontrado"),
        @ApiResponse(responseCode = "401", description = "Token inválido o sin autorización"),
        @ApiResponse(responseCode = "404", description = "Cliente no encontrado")
    })
    public ResponseEntity<Cliente> getClienteByUsername(
            @PathVariable @Parameter(description = "Nombre de usuario del cliente") String user,
            @RequestHeader(value = "Authorization", required = false)
            @Parameter(description = "Token JWT en el encabezado Authorization (formato: Bearer <token>)") String authHeader) {

        String token = this.jwtService.extractToken(authHeader);
        if (token == null || !this.jwtService.validarToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (!(this.jwtService.obtenerUsuario(token).equals(user) || this.jwtService.obtenerTipo(token).equals("admin"))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Cliente cliente = service.getClienteByUsername(user);
        if (cliente == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(cliente);
    }
    
}
