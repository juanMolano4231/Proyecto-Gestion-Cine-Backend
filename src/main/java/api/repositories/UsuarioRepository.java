/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package api.repositories;

import api.models.Administrador;
import api.models.Cliente;
import api.models.Usuario;
import api.models.Usuario;
import api.models.data.ClienteData;
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
import org.springframework.security.crypto.password.PasswordEncoder;
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

    private final PasswordEncoder passwordEncoder;

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(UsuarioRepository.class);

    public UsuarioRepository(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Usuario saveUsuario(Usuario usuario) {
        if (usuario == null || usuario.getUsuario() == null) {
            logger.warn("Usuario nulo o sin nombre de usuario, no se puede guardar.");
            return null;
        }

        if (findByUser(usuario.getUsuario()) != null) {
            logger.info("El usuario '{}' ya existe, se omite la inserción.", usuario.getUsuario());
            return null;
        }

        UsuarioData data = new UsuarioData();
        data.setUsuario(usuario.getUsuario());
        data.setPin(passwordEncoder.encode(usuario.getPin()));
        if (usuario instanceof Administrador) {
            data.setTipo("admin");
        } else {
            data.setTipo("cliente");
        }
        
        entityManager.persist(data);
        entityManager.flush();

        if ("cliente".equalsIgnoreCase(data.getTipo())) {
            ClienteData clienteData = new ClienteData();
            clienteData.setIdUsuario(data.getId());
            clienteData.setTiquetes("[]");

            entityManager.persist(clienteData);
            logger.info("ClienteData creado para el usuario '{}'", data.getUsuario());
        }
        
        return usuario;
    }

    public Usuario login(String username, String rawPin) {
        Usuario usuario = findByUser(username);
        if (usuario == null) {
            return null;
        }
        return passwordEncoder.matches(rawPin, usuario.getPin()) ? usuario : null;
    }

    public List<Usuario> getAllUsuarios() {
        Query query = entityManager.createNativeQuery("SELECT * FROM usuarios_data", UsuarioData.class);
        List<UsuarioData> dataUsuarios = query.getResultList();
        List<Usuario> usuarios = new ArrayList<>();

        for (UsuarioData data : dataUsuarios) {
            Usuario u = new Usuario();

            u.setUsuario(data.getUsuario());
            u.setPin(data.getPin());

            usuarios.add(u);
        }
        return usuarios;
    }

    public Usuario findByUser(String usuario) {
        Query query = entityManager.createNativeQuery("SELECT * FROM usuarios_data WHERE usuario = :usuario", UsuarioData.class);
        query.setParameter("usuario", usuario);
        Usuario usu = null;
        try {
            usu = convertir((UsuarioData) query.getSingleResult());
        } catch (Exception e) {
        }
        return usu;
    }

    public Usuario convertir(UsuarioData data) {
        Usuario u = new Usuario();
        u.setUsuario(data.getUsuario());
        u.setPin(data.getPin());

        return u;
    }

//    public Usuario updateUsuario(String user, Usuario usuario) {
//        Usuario existente = findByUser(user);
//        if (existente != null) {
//            existente.setPin(usuario.getPin());
//            existente.setUsuario(usuario.getUsuario());
//            UsuarioData data = entityManager.find(UsuarioData.class, existente.getId());
//
//            if (data == null) {
//                return null;
//            }
//
//            data.setUsuario(usuario.getUsuario());
//            data.setPin(usuario.getPin());
//            data.setTipo(usuario.getTipo() != null ? usuario.getTipo() : "cliente");
//
//            entityManager.merge(data);
//            return convertir(data);
//        }
//        return null;
//    }

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

    public String consultarTipo(String user) {
        Query query = entityManager.createNativeQuery("SELECT * FROM usuarios_data WHERE usuario = :usuario", UsuarioData.class);
        query.setParameter("usuario", user);
        UsuarioData data = null;
        try {
            data = (UsuarioData) query.getSingleResult();
        } catch (Exception e) {
//            logger.warn("No se puede encontrar el tipo del usuario: {}", user);
            return null;
        }
        return '"' + data.getTipo() + '"';
    }

    public Boolean checkUsername(String user) {
        return getAllUsuarios().stream()
                .anyMatch(u -> u.getUsuario().equals(user));
    }

}
