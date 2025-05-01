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
        Query query = entityManager.createNamedQuery("SELECT * FROM usuarios", Usuario.class);
        return query.getResultList();
    }

    public Usuario findByUser(String user) {
        for (Usuario usuario : baseDeDatosUsuarios) {
            if (usuario.getUsuario().equals(user)) {
                return usuario;
            }
        }
        return null;
    }

    public Usuario postUsuario(String user, Usuario usuario) {
        for (int i = 0; i < baseDeDatosUsuarios.size(); i++) {
            Usuario u = baseDeDatosUsuarios.get(i);
            if (u.getUsuario().equals(user)) {
                baseDeDatosUsuarios.set(i, usuario);
                return usuario;
            }
        }
        return null;
    }

    public Cliente postCliente(String user, Cliente cliente) {
        for (int i = 0; i < baseDeDatosUsuarios.size(); i++) {
            Usuario u = baseDeDatosUsuarios.get(i);
            if (u instanceof Cliente) {
                Cliente c = (Cliente) u;
                if (u.getUsuario().equals(user)) {
                    baseDeDatosUsuarios.set(i, cliente);
                    return cliente;
                }
            }
        }
        return null;
    }

}
