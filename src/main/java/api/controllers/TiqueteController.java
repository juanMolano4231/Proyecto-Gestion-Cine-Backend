/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package api.controllers;

import api.models.data.TiqueteData;
import api.services.JWTService;
import api.services.TiqueteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.Http2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Juan José Molano Franco
 */
@RestController
@RequestMapping("/api/tiquetes")
@Tag(name = "Tiquetes", description = "API para la gestión de tiquetes")
public class TiqueteController {

    private final TiqueteService service;
    private final JWTService jwtService;

    @Autowired
    public TiqueteController(TiqueteService service, JWTService jwtService) {
        this.service = service;
        this.jwtService = jwtService;
    }

    @DeleteMapping("/{idFuncion}/{asiento}")
    @Operation(summary = "Borrar un tiquete", description = "Borra un tiquete según la id de su función y asiento.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Tiquete borrado exitosamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor"),
        @ApiResponse(responseCode = "404", description = "Tiquete no encontrado")
    })
    public ResponseEntity<Void> borrarTiquete(
            @Parameter(description = "ID de la función a la que pertenece el tiquete", required = true)
            @PathVariable int idFuncion,
            @Parameter(description = "Número de asiento del tiquete que se desea eliminar", required = true)
            @PathVariable int asiento,
            @Parameter(description = "Token JWT de autenticación", required = true)
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        String token = this.jwtService.extractToken(authHeader);
        if (token == null || !this.jwtService.validarToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        boolean tiqueteBorrado = service.deleteTiquete(idFuncion, asiento);
        return tiqueteBorrado ? ResponseEntity.status(HttpStatus.NO_CONTENT).build() : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

}
