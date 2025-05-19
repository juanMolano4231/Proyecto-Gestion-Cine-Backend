/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package api.repositories;

import api.models.data.TiqueteData;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.hibernate.internal.CoreLogging;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Juan José Molano Franco
 */

@Repository
public class TiqueteRepository {
    
    @PersistenceContext
    private EntityManager entityManager;
    
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ClienteRepository.class);

    @Transactional
    public boolean deleteTiquete(int idFuncion, int asiento) {
        Query query = entityManager.createNativeQuery(
                "SELECT * FROM tiquetes_data WHERE id_funcion = :idFuncion AND asiento = :asiento", TiqueteData.class);
        query.setParameter("idFuncion", idFuncion);
        query.setParameter("asiento", asiento);
        TiqueteData data = null;
        try {
            data = (TiqueteData) query.getSingleResult();
        } catch (Exception e) {
            logger.warn("Fallo al buscar el tiquete de id_funcion: {} y asiento: {}", idFuncion, asiento);
            e.printStackTrace();
            return false;
        }
        
        entityManager.remove(data);
        return true;
    }

}
