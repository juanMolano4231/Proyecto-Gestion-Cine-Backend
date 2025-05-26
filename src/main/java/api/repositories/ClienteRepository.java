/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package api.repositories;

import api.models.Cliente;
import api.models.Funcion;
import api.models.Tiquete;
import api.models.Usuario;
import api.models.data.ClienteData;
import api.models.data.FuncionData;
import api.models.data.TiqueteData;
import api.models.data.UsuarioData;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import com.google.gson.Gson;

/**
 *
 * @author johan
 */
@Repository
public class ClienteRepository {

    @PersistenceContext
    private EntityManager entityManager;

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ClienteRepository.class);

    public UsuarioData findUserDataById(int id) {
        Query query = entityManager.createNativeQuery("SELECT * FROM usuarios_data WHERE id = :id", UsuarioData.class);
        query.setParameter("id", id);

        UsuarioData usu = null;
        try {
            usu = (UsuarioData) query.getSingleResult();
        } catch (Exception e) {
            logger.warn("No se pudo encontrar el usuarioData de id: {}", id);
        }
        return usu;
    }

    public Usuario findUserById(int id) {
        Query query = entityManager.createNativeQuery("SELECT * FROM usuarios_data WHERE id = :id", UsuarioData.class);
        query.setParameter("id", id);

        Usuario usu = null;
        try {
            usu = convertir((UsuarioData) query.getSingleResult());
        } catch (Exception e) {
            logger.warn("No se pudo encontrar el usuarioData de id: {}, para convertirlo a usuario", id);
        }
        return usu;
    }

    public Usuario convertir(UsuarioData data) {
        Usuario u = new Usuario();
        u.setUsuario(data.getUsuario());
        u.setPin(data.getPin());

        return u;
    }

    public Cliente updateCliente(String user, Cliente cliente) {
        Query queryUsuario = entityManager.createNativeQuery("SELECT * FROM usuarios_data WHERE usuario = :user", UsuarioData.class);
        queryUsuario.setParameter("user", user);
        UsuarioData usuarioData = null;
        try {
            usuarioData = (UsuarioData) queryUsuario.getSingleResult();
        } catch (Exception e) {
            return null;
        }

        Query queryCliente = entityManager.createNativeQuery("SELECT * FROM clientes_data WHERE id_usuario = :idUsuario", ClienteData.class);
        queryCliente.setParameter("idUsuario", usuarioData.getId());
        ClienteData clienteData = null;
        try {
            clienteData = (ClienteData) queryCliente.getSingleResult();
        } catch (Exception e) {
            return null;
        }

        String tiquetesJson = parsearTiquetesAJSON(cliente.getTiquetes());

        clienteData.setTiquetes(tiquetesJson);
        entityManager.merge(clienteData);

        return cliente;
    }

    private String parsearTiquetesAJSON(List<Tiquete> tiquetes) {
        if (tiquetes == null || tiquetes.isEmpty()) {
            return "[]";
        }

        actualizarTiquetesEnDB(tiquetes);
        int[] idsTiquetes = conseguirIdsTiquetes(tiquetes);
        
        return new Gson().toJson(idsTiquetes);
    }

    private int[] conseguirIdsTiquetes(List<Tiquete> tiquetes) {
        int[] idsTiquetes = new int[tiquetes.size()];
        for (int i = 0; i < tiquetes.size(); i++) {
            Tiquete t = tiquetes.get(i);
            Query query = entityManager.createNativeQuery("SELECT * FROM tiquetes_data WHERE asiento = :asiento AND id_funcion = :idFuncion", TiqueteData.class);
            query.setParameter("asiento", t.getAsiento());
            query.setParameter("idFuncion", t.getFuncion().getId());
            TiqueteData data = null;
            try {
                data = (TiqueteData) query.getSingleResult();
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
            idsTiquetes[i] = data.getId();
        }
        return idsTiquetes;
    }

    private void actualizarTiquetesEnDB(List<Tiquete> tiquetes) {
        for (Tiquete t : tiquetes) {
            Query checkQuery = entityManager.createNativeQuery("SELECT * FROM tiquetes_data WHERE asiento = :asiento AND id_funcion = :idFuncion");
            checkQuery.setParameter("asiento", t.getAsiento());
            checkQuery.setParameter("idFuncion", t.getFuncion().getId());
            try {
                checkQuery.getSingleResult();
            } catch (NoResultException e) {
                TiqueteData data = new TiqueteData();
                data.setAsiento(t.getAsiento());
                data.setIdFuncion(t.getFuncion().getId());
                entityManager.persist(data);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        entityManager.flush();
    }

    private List<Tiquete> JSONATiquetes(String json, int idUsuario) {
        List<Tiquete> tiquetes = new ArrayList<>();
        int[] idsTiquetes = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            idsTiquetes = mapper.readValue(json, new TypeReference<int[]>() {
            });
        } catch (Exception e) {
            logger.warn("La lista de tiquetes del cliente con id_usuario: {} no se pudo parsear", idUsuario);
            return null;
        }
        for (int i = 0; i < idsTiquetes.length; i++) {
            int idTiquete = idsTiquetes[i];
            Query queryTiquete = entityManager.createNativeQuery("SELECT * FROM tiquetes_data WHERE id = :id", TiqueteData.class);
            queryTiquete.setParameter("id", idTiquete);
            TiqueteData dataTiquete = null;
            try {
                dataTiquete = (TiqueteData) queryTiquete.getSingleResult();
            } catch (Exception e) {
                logger.warn("No se encontró el tiqueteData de id: {}, del cliente de id_usuar'o: {}, con JSON: {}", idTiquete, idUsuario, json);
                continue;
            }

            int idFuncion = dataTiquete.getIdFuncion();
            Query queryFuncion = entityManager.createNativeQuery("SELECT * FROM funciones_data WHERE id = :id", FuncionData.class);
            queryFuncion.setParameter("id", idFuncion);
            FuncionData dataFuncion = null;
            try {
                dataFuncion = (FuncionData) queryFuncion.getSingleResult();
            } catch (Exception e) {
                logger.warn("No se encontró la funcionData de id: {}, del cliente de id_usuario: {}, del tiquete de id: {}", idFuncion, idUsuario, idTiquete);
                continue;
            }

            Funcion funcion = new Funcion();
            funcion.setAsientos(JSONAAsientos(dataFuncion.getAsientos()));
            funcion.setFin(dataFuncion.getFin());
            funcion.setId(dataFuncion.getId());
            funcion.setInicio(dataFuncion.getInicio());
            funcion.setTitulo(dataFuncion.getTitulo());

            Tiquete tiquete = new Tiquete(funcion, dataTiquete.getAsiento());
            tiquetes.add(tiquete);
        }
        return tiquetes;
    }

    private boolean[] JSONAAsientos(String asientos) {
        boolean[] asientosBoolean = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            asientosBoolean = mapper.readValue(asientos, boolean[].class);
        } catch (Exception e) {
            logger.warn("No se pudo parsear los asientos de JSON: {}", asientos);
        }
        return asientosBoolean;
    }

    public Cliente getClienteByUsername(String user) {
        Query queryUsuario = entityManager.createNativeQuery("SELECT * FROM usuarios_data WHERE usuario = :usuario", UsuarioData.class);
        queryUsuario.setParameter("usuario", user);
        UsuarioData usuarioData = null;
        try {
            usuarioData = (UsuarioData) queryUsuario.getSingleResult();
        } catch (Exception e) {
            return null;
        }
        
        Query queryCliente = entityManager.createNativeQuery("SELECT * FROM clientes_data WHERE id_usuario = :idUsuario", ClienteData.class);
        queryCliente.setParameter("idUsuario", usuarioData.getId());
        ClienteData clienteData = null;
        try {
            clienteData = (ClienteData) queryCliente.getSingleResult();
        } catch (Exception e) {
            return null;
        }
        
        Cliente cliente = new Cliente(usuarioData.getUsuario(), usuarioData.getPin());

        cliente.setTiquetes(JSONATiquetes(clienteData.getTiquetes(), clienteData.getIdUsuario()));

        return cliente;
    }

}
