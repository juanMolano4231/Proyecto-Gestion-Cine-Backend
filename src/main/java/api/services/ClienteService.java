/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package api.services;

import api.models.Cliente;
import api.repositories.ClienteRepository;
import api.repositories.UsuarioRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author johan
 */
@Service
public class ClienteService {

    private final UsuarioRepository usuarioRepository;
    private final ClienteRepository clienteRepository;

    @Autowired
    public ClienteService(UsuarioRepository usuarioRepository, ClienteRepository clienteRepository) {
        this.usuarioRepository = usuarioRepository;
        this.clienteRepository = clienteRepository;
    }

    public List<Cliente> getAllClientes() {
        return clienteRepository.getAllClientes(usuarioRepository.getAllUsuarios());
    }
}
