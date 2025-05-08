/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package api.repositories;

import api.models.Cliente;
import api.models.Tiquete;
import api.models.Usuario;
import api.models.data.ClienteData;
import api.models.data.UsuarioData;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

/**
 *
 * @author johan
 */
@Repository
public class ClienteRepository {

    @PersistenceContext
    private EntityManager entityManager;

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ClienteRepository.class);

    public List<Cliente> getAllClientes() {
        List<Cliente> clientes = new ArrayList<>();

        Query queryClientes = entityManager.createNativeQuery("SELECT * FROM clientes_data", ClienteData.class);
        List<ClienteData> dataClientes = queryClientes.getResultList();

        for (ClienteData data : dataClientes) {
            Usuario u = findByIdUser(data.getIdUsuario());
            if (u == null) {
                continue;
            }
            Cliente c = new Cliente(u.getUsuario(), u.getPin());
            c.setId(u.getId());
            c.setTipo(u.getTipo());

            try {
                ObjectMapper mapper = new ObjectMapper();
                List<Tiquete> tiquetes = mapper.readValue(data.getTiquetes(), new TypeReference<List<Tiquete>>() {
                });
                c.setTiquetes(tiquetes);
            } catch (Exception e) {
                c.setTiquetes(List.of());
            }

            clientes.add(c);
        }

        return clientes;
    }

    public Usuario findByIdUser(int id) {
        Query query = entityManager.createNativeQuery("SELECT * FROM usuarios_data WHERE id = :id", UsuarioData.class);
        query.setParameter("id", id);

        List<UsuarioData> result = query.getResultList();
        if (result.isEmpty()) {
            return null;
        }
        return convertir(result.get(0));
    }

    public Usuario convertir(UsuarioData data) {
        Usuario u = new Usuario();
        u.setId(data.getId());
        u.setUsuario(data.getUsuario());
        u.setPin(data.getPin());
        u.setTipo(data.getTipo());

        return u;
    }

    public Cliente updateCliente(int idUsuario, List<Tiquete> nuevosTiquetes) {
        Query query = entityManager.createNativeQuery(
                "SELECT * FROM clientes_data WHERE id_usuario = :id", ClienteData.class);
        query.setParameter("id", idUsuario);

        List<ClienteData> result = query.getResultList();
        if (result.isEmpty()) {
            logger.warn("No se encontró cliente con id_usuario = {}", idUsuario);
            return null;
        }

        ClienteData clienteData = result.get(0);

        try {
            ObjectMapper mapper = new ObjectMapper();
            String tiquetesJson = mapper.writeValueAsString(nuevosTiquetes);

            clienteData.setTiquetes(tiquetesJson);
            entityManager.merge(clienteData);

            Usuario usuarioBase = findByIdUser(idUsuario);
            if (usuarioBase == null) {
                return null;
            }

            Cliente cliente = new Cliente(usuarioBase.getUsuario(), usuarioBase.getPin());
            cliente.setId(usuarioBase.getId());
            cliente.setTipo(usuarioBase.getTipo());
            cliente.setTiquetes(nuevosTiquetes);

            return cliente;
        } catch (Exception e) {
            logger.error("Error actualizando cliente con id_usuario = {}", idUsuario, e);
            return null;
        }
    }

}
