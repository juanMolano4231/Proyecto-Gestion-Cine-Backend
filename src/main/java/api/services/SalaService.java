/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package api.services;

import api.repositories.SalaRepository;
import api.models.Funcion;
import api.models.Sala;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Juan José Molano Franco
 */
@Service
public class SalaService {

    private final SalaRepository repository;

    @Autowired
    public SalaService(SalaRepository repository) {
        this.repository = repository;
        initSampleData();
    }
    private void initSampleData() {
        Sala s1 = new Sala(30);
        Sala s2 = new Sala(46);
        Funcion f = new Funcion("", "", "a", s2.getAsientos());
        f.getAsientos()[0] = true;
        s2.getFunciones().add(f);
        saveSala(s1);
        saveSala(s2);
    }

    public Sala saveSala(Sala sala) {
        repository.saveSala(sala);
        return sala;
    }

    public List<Sala> getSalas() {
        return repository.getSalas();
    }

    public Sala findSala(int index) {
        return repository.findSala(index);
    }

    public void deleteSala(int index) {
        repository.deleteSala(index);
    }
}
