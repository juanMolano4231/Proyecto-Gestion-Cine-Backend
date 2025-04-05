/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package api.repositories;

import api.models.Funcion;
import api.models.Sala;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Juan José Molano Franco
 */
@Repository
public class SalaRepository {

    private final List<Sala> baseDeDatosSalas = new ArrayList<>();

    public void saveSala(Sala sala) {
        sala.setId(idSalaUnica());
        baseDeDatosSalas.add(sala);
    }

    public List<Sala> getSalas() {
        return baseDeDatosSalas;
    }

    public Sala findSala(int index) {
        try {
            return baseDeDatosSalas.get(index);
        } catch (Exception e) {
            return null;
        }
    }

    public void deleteSala(int index) {
        baseDeDatosSalas.remove(index);
    }

    public int idFuncionUnica() {
        for (int i = 0;; i++) {
            boolean unica = true;
            for (Sala s : baseDeDatosSalas) {
                boolean match = idFuncionRepetida(s, i);
                if (match) {
                    unica = false;
                }
            }
            if (unica) {
                return i;
            }
        }
    }

    private int idSalaUnica() {
        for (int i = 0;; i++) {
            boolean match = idSalaRepetida(i);
            if (! match) {
                return i;
            }
        }
    }

    private boolean idFuncionRepetida(Sala s, int i) {
        return s.getFunciones().stream().anyMatch(f -> f.getId() == i);
    }

    private boolean idSalaRepetida(int i) {
        return baseDeDatosSalas.stream().anyMatch(s -> s.getId() == i);
    }

}
