/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package api.repositories;

import api.models.Cliente;
import api.models.Usuario;
import api.models.Usuario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;

/**
 *
 * @author Juan José Molano Franco
 */
@Repository
@Transactional
public class UsuarioRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public void saveUsuario(Usuario usuario) {
        entityManager.persist(usuario);
    }

    public List<Usuario> getAllUsuarios() {
        Query query = entityManager.createNativeQuery("SELECT * FROM usuarios", Usuario.class);
        return query.getResultList();
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
}
