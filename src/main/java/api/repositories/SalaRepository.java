/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package api.repositories;

import api.models.Sala;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Juan José Molano Franco
 */
@Repository
public class SalaRepository {
    private final List<Sala> baseDeDatosSalas = new ArrayList<>();

    public void saveSala(Sala sala) {
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

}
