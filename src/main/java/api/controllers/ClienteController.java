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

    @GetMapping
    @Operation(summary = "Obtener todos los clientes", description = "Devuelve una lista de todos los clientes registrados.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de clientes obtenida con éxito"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<Cliente>> getAllClientes() {
        List<Cliente> clientes = service.getAllClientes();
        return new ResponseEntity<>(clientes, HttpStatus.OK);
    }
    
    @PostMapping("/{user}")
    @Operation(summary = "Actualizar cliente", description = "Actualiza un cliente según los datos proporcionados.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = " Cliente actualizado con éxito"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<Cliente> postCliente(@PathVariable @Parameter(description = "Username del cliente") String user,
            @RequestBody @Parameter(description = "Datos del cliente a actualizar") Cliente cliente) {
        Cliente nuevoCliente = service.postCliente(user, cliente);
        if (nuevoCliente == null) {
            return new ResponseEntity<>(nuevoCliente, HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(nuevoCliente, HttpStatus.CREATED);
        }
    }
    
    @PostMapping
    @Operation(summary = "Crear un nuevo cliente", description = "Crea un nuevo cliente con los datos proporcionados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cliente creado con éxito"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<Cliente> createCliente(@RequestBody @Parameter(description = "Datos del cliente a crear") Cliente cliente) {
        Cliente newUsuario = service.saveCliente(cliente);
        return new ResponseEntity<>(newUsuario, HttpStatus.CREATED);
    }

}
