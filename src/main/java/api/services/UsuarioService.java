/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package api.services;

import api.repositories.UsuarioRepository;
import api.models.Administrador;
import api.models.Cliente;
import api.models.Usuario;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 *
 * @author Juan José Molano Franco
 */
@Service
public class UsuarioService {

    private final UsuarioRepository repository;

    @Autowired
    public UsuarioService(UsuarioRepository repository) {
        this.repository = repository;
        initSampleData();
    }

    private void initSampleData() {
        Usuario juan = new Administrador("juan1234", String.valueOf(1234));
        Usuario johan = new Cliente("johan1234", String.valueOf(1234));
        saveUsuario(juan);
        saveUsuario(johan);
    }

    public Usuario saveUsuario(Usuario usuario) {
        return repository.saveUsuario(usuario);
    }

    public Usuario login(String username, String pin) {
        return repository.login(username, pin);
    }

    public List<Usuario> getAllUsuarios() {
        return repository.getAllUsuarios();
    }

    public Usuario findByUser(String user) {
        return repository.findByUser(user);
    }

//    public Usuario updateUsuario(String user, Usuario usuario) {
//        return repository.updateUsuario(user, usuario);
//    }

//    Cliente postCliente(String user, Cliente cliente) {
//        return repository.postCliente(user, cliente);
//    }

    public String consultarTipo(String user) {
        return repository.consultarTipo(user);
    }
}
