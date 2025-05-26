/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package api.services;

import api.models.data.TiqueteData;
import api.repositories.TiqueteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Juan José Molano Franco
 */

@Service
public class TiqueteService {
    
    private final TiqueteRepository repository;

    @Autowired
    public TiqueteService(TiqueteRepository repository) {
        this.repository = repository;
    }
    
    public boolean deleteTiquete(int idFuncion, int asiento) {
        return repository.deleteTiquete(idFuncion, asiento);
    }

}
