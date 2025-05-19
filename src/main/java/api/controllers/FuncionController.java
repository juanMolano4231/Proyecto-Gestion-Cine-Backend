/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package api.controllers;

import api.models.Funcion;
import api.services.FuncionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/api/funciones")
@Tag(name = "Funciones", description = "API para la gestión de funciones")
public class FuncionController {
    
    private final FuncionService service;

    @Autowired
    public FuncionController(FuncionService service) {
        this.service = service;
    }

    @PutMapping("/{idFuncion}")
    @Operation(summary = "Actualizar una función", description = "Actualiza una función completamente.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Función actualizada exitosamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor"),
        @ApiResponse(responseCode = "400", description = "Funcion proporcionada malformada")
    })
    public ResponseEntity<Void> updateFuncion(@PathVariable int idFuncion, @RequestBody Funcion funcion) {
        Funcion updatedFuncion = service.updateFuncion(idFuncion, funcion);
        return updatedFuncion == null ? ResponseEntity.status(HttpStatus.BAD_REQUEST).build() : ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    
}
