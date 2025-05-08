/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package api.services;

import api.models.Cliente;
import api.models.Usuario;
import api.repositories.ClienteRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author johan
 */
@Service
public class ClienteService {

    private final UsuarioService usuarioService;
    private final ClienteRepository clienteRepository;

    @Autowired
    public ClienteService(UsuarioService usuarioService, ClienteRepository clienteRepository) {
        this.usuarioService = usuarioService;
        this.clienteRepository = clienteRepository;
    }

    public List<Cliente> getAllClientes() {
        return clienteRepository.getAllClientes();
    }

    public Cliente updateCliente(int id, List<api.models.Tiquete> tiquetes) {
        return clienteRepository.updateCliente(id, tiquetes);
    }

}
