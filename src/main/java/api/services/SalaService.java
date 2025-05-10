/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package api.services;

import api.controllers.SalaController;
import api.repositories.SalaRepository;
import api.models.Funcion;
import api.models.Sala;
import java.util.List;
import javax.swing.JOptionPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Juan José Molano Franco
 */
@Service
public class SalaService {

    private final SalaRepository repository;
    private final FuncionService funcionService;

    @Autowired
    public SalaService(SalaRepository repository, FuncionService funcionService) {
        this.repository = repository;
        this.funcionService = funcionService;
    }

    public Sala saveSala(Sala sala) {
        repository.saveSala(sala);
        return sala;
    }

    public List<Sala> getSalas() {
        return repository.getSalas();
    }

    public Sala findSala(int id) {
        return repository.findSala(id);
    }

    public void deleteSala(Sala sala) {
        repository.deleteSala(sala);
    }

    public Funcion saveFuncion(int idSala, String[] datos) {
        int idFuncion = funcionService.save(datos, idSala);
        return repository.saveFuncion(idSala, idFuncion);
    }

    public Sala patchSala(int id, Sala sala) {
//        return repository.patchSala(id, sala);
throw new UnsupportedOperationException();
    }

}
