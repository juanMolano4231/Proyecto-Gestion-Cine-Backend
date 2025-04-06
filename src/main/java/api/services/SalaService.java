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

    @Autowired
    public SalaService(SalaRepository repository) {
        this.repository = repository;
        initSampleData();
    }

    private void initSampleData() {
        Sala s1 = new Sala(30);
        saveSala(s1);
        Sala s2 = new Sala(46);
        saveSala(s2);  // PRIMERO GUARDAR SALA ANTES DE AÑADIR FUNCIONES PARA QUE LAS ID SE ASIGNEN CORRECTAMENTE

        Funcion f = new Funcion("", "", "Zootopia", s2.getAsientos(), repository.idFuncionUnica());
        f.getAsientos()[0] = true;
        s2.getFunciones().add(f);
        Funcion f2 = new Funcion("", "", "Big hero", s2.getAsientos(), repository.idFuncionUnica());
        s2.getFunciones().add(f2);
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

    public Funcion saveFuncion(int id, String[] datos) {
        return repository.saveFuncion(id, datos);
    }

}
