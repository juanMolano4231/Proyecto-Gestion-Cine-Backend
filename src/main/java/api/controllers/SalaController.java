/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package api.controllers;

import api.models.Cliente;
import api.models.Funcion;
import api.services.SalaService;
import api.models.Sala;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import javax.swing.JOptionPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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
@RequestMapping("/api/salas")
@Tag(name = "Salas", description = "API para la gestión de salas")

public class SalaController {

    private final SalaService service;

    @Autowired
    public SalaController(SalaService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Obtener todas las salas", description = "Devuelve una lista de todas las salas existentes.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de salas obtenida con éxito"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<Sala>> getSalas() {
        List<Sala> Salas = service.getSalas();
        return new ResponseEntity<>(Salas, HttpStatus.OK);
    }

    @PostMapping
    @Operation(summary = "Crear una nueva sala", description = "Crea una nueva sala con los datos proporcionados.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Sala creado con éxito"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<Sala> createSala(@RequestBody @Parameter(description = "Datos de la sala a crear") Sala sala) {
        Sala nuevaSala = service.saveSala(sala);
        return new ResponseEntity<>(nuevaSala, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una sala", description = "Elimina una sala por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Sala eliminada con éxito"),
        @ApiResponse(responseCode = "404", description = "Sala no encontrado")
    })
    public ResponseEntity<Void> deleteSala(@PathVariable @Parameter(description = "ID de la sala") int id) {
        Sala salaExistente = service.findSala(id);
        if (salaExistente != null) {
            service.deleteSala(salaExistente);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Crear una nueva función", description = "Crea una nueva función para cierta sala con los datos proporcionados.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Función creado con éxito"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<Void> createFuncion(@PathVariable @Parameter(description = "ID de la sala") int id,
            @RequestBody @Parameter(description = "Datos de la función a crear (título, fecha inicio, fecha fin).") String[] datos) {
        Funcion nuevaFuncion = service.saveFuncion(id, datos);
        if (nuevaFuncion == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Actualizar sala", description = "Actualiza una sala según los datos proporcionados.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Sala actualizada con éxito"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<Sala> patchSala(@PathVariable @Parameter(description = "ID de la sala") int id,
            @RequestBody @Parameter(description = "Datos de la sala a actualizar") Sala sala) {
        Sala nuevaSala = service.patchSala(id, sala);
        if (nuevaSala == null) {
            return new ResponseEntity<>(nuevaSala, HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(nuevaSala, HttpStatus.CREATED);
        }
    }
}
