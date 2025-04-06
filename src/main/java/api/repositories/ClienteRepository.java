/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package api.repositories;

import api.models.Cliente;
import api.models.Usuario;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 *
 * @author johan
 */
@Repository
public class ClienteRepository {

    public List<Cliente> getAllClientes(List<Usuario> usuarios) {
        List<Cliente> clientes = new ArrayList<>();
        for (Usuario u : usuarios) {
            if (u instanceof Cliente) {
                clientes.add((Cliente) u);
            }
        }
        return clientes;
    }

}
