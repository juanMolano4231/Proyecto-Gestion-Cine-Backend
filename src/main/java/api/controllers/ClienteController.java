/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package api.controllers;

import api.models.Cliente;
import api.models.Usuario;
import api.services.ClienteService;
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
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author johan
 */
@RestController
@RequestMapping("/api/clientes_data")
@Tag(name = "Clientes", description = "API para la gestión de clientes")
public class ClienteController {

    private final ClienteService service;

    @Autowired
    public ClienteController(ClienteService service) {
        this.service = service;
        System.out.println(">>> ClienteController inicializado");

    }

    @Transactional
    @GetMapping
    public List<Cliente> getAllClientes() {
        return service.getAllClientes();
    }

    @Transactional
    @PutMapping("/{id}")
    public ResponseEntity<Cliente> updateClienteTiquetes(
            @PathVariable int id,
            @RequestBody List<api.models.Tiquete> nuevosTiquetes) {

        Cliente actualizado = service.updateCliente(id, nuevosTiquetes);
        if (actualizado == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(actualizado);
    }

}
