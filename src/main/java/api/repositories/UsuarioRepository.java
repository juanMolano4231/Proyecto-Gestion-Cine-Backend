/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package api.repositories;

import api.models.Usuario;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Juan José Molano Franco
 */

@Repository
public class UsuarioRepository {
    private final List<Usuario> baseDeDatosUsuarios = new ArrayList<>();

    public void saveUsuario(Usuario usuario) {
        baseDeDatosUsuarios.add(usuario);
    }
    
    public List<Usuario> getAllUsuarios() {
        return baseDeDatosUsuarios;
    }

    public Usuario findByUser(String user) {
        for (Usuario usuario : baseDeDatosUsuarios) {
            if(usuario.getUsuario().equals(user)) {
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

}
