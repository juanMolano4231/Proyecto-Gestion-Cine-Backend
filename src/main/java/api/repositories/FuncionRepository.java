/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package api.repositories;

import api.models.Funcion;
import api.models.data.FuncionData;
import api.models.data.SalaData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Juan José Molano Franco
 */

@Repository
public class FuncionRepository {
    
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public int save(String[] datos, int idSala) {
        Query getSalaQuery = entityManager.createNativeQuery("SELECT * FROM salas_data WHERE id = :id", SalaData.class);
        getSalaQuery.setParameter("id", idSala);
        SalaData sala = (SalaData) getSalaQuery.getSingleResult();
        
        FuncionData funcion = new FuncionData();
        // datos = {titulo, inicio, fin}
        funcion.setAsientos(makeJSONBooleanArray(sala.getAsientos()));
        funcion.setTitulo(datos[0]);
        funcion.setInicio(datos[1]);
        funcion.setFin(datos[2]);
        
        entityManager.persist(funcion);
        
        return funcion.getId();
    }
    
    private String makeJSONBooleanArray(Integer asientos) {
        if (asientos == 0) {
            return "[]";
        }
        
        boolean[] arr = new boolean[asientos];
        
        String JSON = "[";
        for (int i = 0; i < arr.length; i++) {
            JSON += arr[i] + ", ";
        }
        return JSON.substring(0, JSON.length() - 2) + "]";
    }

    @Transactional
    public Funcion updateFuncion(int idFuncion, Funcion funcion) {
        Query query = entityManager.createNativeQuery("UPDATE funciones_data SET asientos = :asientos WHERE id = :id", FuncionData.class);
        query.setParameter("asientos", asientosToJSON(funcion.getAsientos()));
        query.setParameter("id", idFuncion);
        int success = query.executeUpdate();
        return success > 0 ? funcion : null;
    }

    private String asientosToJSON(boolean[] asientos) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(asientos);
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
            return null;
        }
    }

}
