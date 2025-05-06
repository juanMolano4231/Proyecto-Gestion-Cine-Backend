/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package api.repositories;

import api.models.Funcion;
import api.models.Sala;
import api.models.data.FuncionData;
import api.models.data.SalaData;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.swing.JOptionPane;
import org.springframework.stereotype.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;

/**
 *
 * @author Juan José Molano Franco
 */
@Repository
public class SalaRepository {

    @PersistenceContext
    private EntityManager entityManager;

    private final List<Sala> baseDeDatosSalas = new ArrayList<>();

    private static final Logger logger = LoggerFactory.getLogger(SalaRepository.class);

    public void saveSala(Sala sala) {
        sala.setId(idSalaUnica());
        baseDeDatosSalas.add(sala);
    }

    public List<Sala> getSalas() {
        Query query = entityManager.createNativeQuery("SELECT * FROM salas_data", SalaData.class);
        List<SalaData> dataSalas = query.getResultList();
        List<Sala> salas = new ArrayList<>();
        for (SalaData data : dataSalas) {
            List<Integer> idsFunciones = parseFuncionJSON(data.getFunciones());

            Sala sala = new Sala();
            sala.setId(data.getId());
            sala.setAsientos(data.getAsientos());
            sala.setFunciones(findFuncionesById(idsFunciones));

            salas.add(sala);
        }
        return salas;
    }

    public Sala findSala(int id) {
//        for (Sala s : baseDeDatosSalas) {
//            if (s.getId() == id) {
//                return s;
//            }
//        }
//        return null;
        return baseDeDatosSalas.stream()
                .filter(s -> s.getId() == id)
                .findAny()
                .orElse(null);
    }

    public void deleteSala(Sala sala) {
        baseDeDatosSalas.remove(sala);
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
            if (!match) {
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

    public Funcion saveFuncion(int id, String[] datos) {
        Funcion funcion = null;
        for (Sala s : baseDeDatosSalas) {
            if (s.getId() == id) {
                funcion = new Funcion(datos[1], datos[2], datos[0], s.getAsientos(), idFuncionUnica());
                s.getFunciones().add(funcion);
                return funcion;
            }
        }
        return null;
    }

    private void print(String message) {
        JOptionPane.showMessageDialog(null, message);
    }

    public void printSalas() {
        String message = "";
        for (Sala s : baseDeDatosSalas) {
            message += "Sala " + s.getId() + "\n";
            for (Funcion f : s.getFunciones()) {
                message += "    Funcion " + f.getId() + "\n";
            }
        }
        print(message);
    }

    public Sala patchSala(int id, Sala sala) {
        for (int i = 0; i < baseDeDatosSalas.size(); i++) {
            Sala s = baseDeDatosSalas.get(i);
            if (s.getId() == id) {
                baseDeDatosSalas.set(i, sala);
                return sala;
            }
        }
        return null;
    }

    private List<Integer> parseFuncionJSON(String funciones) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(funciones, new TypeReference<List<Integer>>() {
            });
        } catch (Exception e) {
            return null;
        }
    }

    private List<Funcion> findFuncionesById(List<Integer> idsFunciones) {
        ArrayList<Funcion> funciones = new ArrayList<>();
        for (Integer id : idsFunciones) {
            Query query = entityManager.createNativeQuery("SELECT * FROM funciones_data WHERE id = :id", FuncionData.class);
            query.setParameter("id", id.intValue());
            FuncionData data = (FuncionData) query.getSingleResult(); // Si no hay tira EmptyResultDataAccessException 

            Funcion f = new Funcion();

            f.setFin(data.getFin());
            f.setInicio(data.getInicio());
            f.setTitulo(data.getTitulo());
            f.setId(data.getId());
            f.setAsientos(parseBooleanJSON(data.getAsientos()));

            funciones.add(f);
        }
        return funciones;
    }

    private boolean[] parseBooleanJSON(String asientos) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(asientos, boolean[].class);
        } catch (Exception e) {
            return null;
        }
    }

}
