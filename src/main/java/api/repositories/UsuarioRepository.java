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
    
    @Transactional
    public void saveUsuario(Usuario usuario) {
        usuario.setId(idUsuarioUnico());
        baseDeDatosUsuarios.add(usuario);
    }

    public List<Usuario> getAllUsuarios() {
        Query query = entityManager.createNativeQuery("SELECT * FROM usuarios", Usuario.class);
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

    public Usuario findByUser(String user) {
        Query query = entityManager.createNativeQuery("SELECT * FROM usuarios WHERE usuario = :usuario", Usuario.class);
        query.setParameter("usuario", user);
        List<Usuario> result = query.getResultList();
        return result.isEmpty() ? null : result.get(0);
    }

    public Usuario updateUsuario(String user, Usuario usuario) {
        Usuario existente = findByUser(user);
        if (existente != null) {
            existente.setPin(usuario.getPin());
            existente.setUsuario(usuario.getUsuario());
            return entityManager.merge(existente);
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

    private List<Usuario> findUsuarioById(List<Integer> idsUsuarios) {
        ArrayList<Usuario> usuarios = new ArrayList<>();
        for (Integer id : idsUsuarios) {
            Query query = entityManager.createNativeQuery("SELECT * FROM usuarios_data WHERE id = :id", UsuarioData.class);
            query.setParameter("id", id.intValue());
            UsuarioData data = (UsuarioData) query.getSingleResult();

            Usuario u = new Usuario();

            u.setUsuario(data.getUsuario());
            u.setPin(data.getPin());
            u.setId(data.getId());
            u.setTipo(data.getTipo());
        }
        return usuarios;
    }
    
    private int idUsuarioUnico() {
        for (int i = 0;; i++) {
            boolean match = idUsuarioRepetido(i);
            if (!match) {
                return i;
            }
        }
    }
    
    private boolean idUsuarioRepetido(int i) {
        return baseDeDatosUsuarios.stream().anyMatch(u -> u.getId() == i);
    }
    
}
