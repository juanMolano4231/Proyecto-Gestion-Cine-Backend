/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package api.controllers;

import api.models.Cliente;
import api.services.ClienteService;
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

    @Autowired
    public ClienteController(ClienteService service) {
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

}