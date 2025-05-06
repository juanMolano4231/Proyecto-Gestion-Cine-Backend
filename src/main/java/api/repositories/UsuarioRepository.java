/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package api.repositories;

import api.models.Cliente;
import api.models.Usuario;
import api.models.Usuario;
import api.models.data.UsuarioData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;

/**
 *
 * @author Juan José Molano Franco
 */
@Repository
public class UsuarioRepository {

    @PersistenceContext
    private EntityManager entityManager;

    private final List<Usuario> baseDeDatosUsuarios = new ArrayList<>();

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(UsuarioRepository.class);

    @Transactional
    public void saveUsuario(Usuario usuario) {
        if (usuario == null || usuario.getUsuario() == null) {
            logger.warn("Usuario nulo o sin nombre de usuario, no se puede guardar.");
            return;
        }

        if (findByUser(usuario.getUsuario()) != null) {
            logger.info("El usuario '{}' ya existe, se omite la inserción.", usuario.getUsuario());
            return;
        }

        UsuarioData data = new UsuarioData();
        data.setUsuario(usuario.getUsuario());
        data.setPin(usuario.getPin());
        data.setTipo(usuario.getTipo() == null || usuario.getTipo().isBlank() ? "cliente" : usuario.getTipo());

        entityManager.persist(data);
        entityManager.flush();
        usuario.setId(data.getId());
    }

    public List<Usuario> getAllUsuarios() {
        Query query = entityManager.createNativeQuery("SELECT * FROM usuarios_data", UsuarioData.class);
        List<UsuarioData> dataUsuarios = query.getResultList();
        List<Usuario> usuarios = new ArrayList<>();

        for (UsuarioData data : dataUsuarios) {
            List<Integer> idsUsuarios = ParseUsuarioJSON(data.getUsuario());

            Usuario u = new Usuario();

            u.setId(data.getId());
            u.setUsuario(data.getUsuario());
            u.setPin(data.getPin());
            u.setTipo(data.getTipo());

            usuarios.add(u);
        }
        return usuarios;
    }

    public Usuario findByUser(String usuario) {
        Query query = entityManager.createNativeQuery("SELECT * FROM usuarios_data WHERE usuario = :usuario", UsuarioData.class);
        query.setParameter("usuario", usuario);

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

    public Usuario updateUsuario(String user, Usuario usuario) {
        Usuario existente = findByUser(user);
        if (existente != null) {
            existente.setPin(usuario.getPin());
            existente.setUsuario(usuario.getUsuario());
            UsuarioData data = entityManager.find(UsuarioData.class, existente.getId());

            if (data == null) {
                return null;
            }

            data.setUsuario(usuario.getUsuario());
            data.setPin(usuario.getPin());
            data.setTipo(usuario.getTipo() != null ? usuario.getTipo() : "cliente");

            entityManager.merge(data);
            return convertir(data);
        }
        return null;
    }

//    public Cliente postCliente(String user, Cliente cliente) {
//        for (int i = 0; i < baseDeDatosUsuarios.size(); i++) {
//            Usuario u = baseDeDatosUsuarios.get(i);
//            if (u instanceof Cliente) {
//                Cliente c = (Cliente) u;
//                if (u.getUsuario().equals(user)) {
//                    baseDeDatosUsuarios.set(i, cliente);
//                    return cliente;
//                }
//            }
//        }
//        return null;
//    }
    private List<Integer> ParseUsuarioJSON(String usuarios) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(usuarios, new TypeReference<List<Integer>>() {
            });
        } catch (JsonProcessingException ex) {
            return null;
        }
    }

}
