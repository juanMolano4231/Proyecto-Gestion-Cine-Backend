/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package api.services;

import api.repositories.FuncionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Juan José Molano Franco
 */

@Service
public class FuncionService {

    private final FuncionRepository repository;
    
    @Autowired
    public FuncionService(FuncionRepository repository) {
        this.repository = repository;
    }

    int save(String[] datos, int idSala) {
        return repository.save(datos, idSala);
    }
}
