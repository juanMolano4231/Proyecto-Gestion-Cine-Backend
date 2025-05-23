/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package api.controllers;

import api.models.Cliente;
import api.services.ClienteService;
import api.services.JWTService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import java.util.List;
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

    @Autowired
    public ClienteController(ClienteService service, JWTService jwtService) {
        this.jwtService = jwtService;
        this.service = service;

    }

    @Transactional
    @GetMapping
    public List<Cliente> getAllClientes() {
        return service.getAllClientes();
    }

    @Transactional
    @PostMapping("/{user}")
    public ResponseEntity<Cliente> postCliente(
            @PathVariable String user,
            @RequestBody Cliente cliente) {

        Cliente actualizado = service.updateCliente(user, cliente);
        if (actualizado == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(actualizado);
    }
    
    @Transactional
    @PostMapping
    public ResponseEntity<Cliente> createCliente(@RequestBody @Parameter(description = "Datos del cliente a crear") Cliente cliente) {
        Cliente c =  service.saveCliente(cliente);
        return c == null ? ResponseEntity.badRequest().build() : ResponseEntity.ok(cliente);
    }
    
    @Transactional
    @GetMapping("/{user}")
    public ResponseEntity<Cliente> getClienteByUsername(
            @PathVariable String user,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        
        String token = this.jwtService.extractToken(authHeader);
        if (token == null || !this.jwtService.validarToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (   !( this.jwtService.obtenerUsuario(token).equals(user) || this.jwtService.obtenerTipo(token).equals("admin") )   ) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Cliente actualizado = service.getClienteByUsername(user);
        if (actualizado == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(actualizado);
    }
    
}
