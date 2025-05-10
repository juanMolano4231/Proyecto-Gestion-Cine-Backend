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
import jakarta.transaction.Transactional;
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

    @Transactional
    public void saveSala(Sala sala) {
        SalaData data = new SalaData();
        data.setAsientos(sala.getAsientos());
        data.setFunciones(funcionesToJSON(sala.getFunciones()));
        
        entityManager.persist(data);
    }

    @Transactional
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

    @Transactional
    public Sala findSala(int id) {
        Query query = entityManager.createNativeQuery("SELECT * FROM salas_data WHERE id = :id", SalaData.class);
        query.setParameter("id", id);
        SalaData data = null;
        try {
            data = (SalaData) query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
        
        List<Integer> idsFunciones = parseFuncionJSON(data.getFunciones());

        Sala sala = new Sala();
        sala.setId(data.getId());
        sala.setAsientos(data.getAsientos());
        sala.setFunciones(findFuncionesById(idsFunciones));
        
        return sala;
    }

    @Transactional
    public void deleteSala(Sala sala) {
        Integer id = sala.getId();
        Query query = entityManager.createNativeQuery("DELETE FROM salas_data WHERE id = :id");
        query.setParameter("id", id);
        query.executeUpdate();
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

    @Transactional
    public Funcion saveFuncion(int idSala, int idFuncion) {
        Query getQuery = entityManager.createNativeQuery("SELECT * FROM salas_data WHERE id = :id", SalaData.class);
        getQuery.setParameter("id", idSala);
        SalaData sala = (SalaData) getQuery.getSingleResult();
        
        String funciones = sala.getFunciones();
        if (funciones.equals("[]")) {
            funciones = funciones.substring(0, funciones.length() - 1) + idFuncion + "]";
        } else {
            funciones = funciones.substring(0, funciones.length() - 1) + ", " + idFuncion + "]";
        }
        
        logger.info("funciones: {}", funciones);
        
        Query updateQuery = entityManager.createNativeQuery(
                "UPDATE salas_data SET funciones = :funciones WHERE id = :idSala"
        );
        updateQuery.setParameter("funciones", funciones);
        updateQuery.setParameter("idSala", idSala);
        int updated = updateQuery.executeUpdate();
        
        if (updated > 0) {
            Query funcionQuery = entityManager.createNativeQuery("SELECT * FROM funciones_data WHERE id = :id", FuncionData.class);
            funcionQuery.setParameter("id", idFuncion);
            FuncionData funcionData = (FuncionData) funcionQuery.getSingleResult();

            Funcion funcion = new Funcion();

            funcion.setFin(funcionData.getFin());
            funcion.setInicio(funcionData.getInicio());
            funcion.setTitulo(funcionData.getTitulo());
            funcion.setId(funcionData.getId());
            funcion.setAsientos(parseBooleanJSON(funcionData.getAsientos()));

            return funcion;
        } else {
            return null;
        }
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
    
    private String funcionesToJSON(List<Funcion> funciones) {
        if (funciones == null || funciones.size() == 0) {
            return "[]";
        }
        String JSON = "[";
        for (Funcion f : funciones) {
            JSON += f.getId() + ", ";
        }
        return JSON.substring(0, JSON.length() - 2) + "]";
    }

}
