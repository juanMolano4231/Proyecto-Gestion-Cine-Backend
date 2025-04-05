/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package api.models;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Juan José Molano Franco
 */
public class Cliente extends Usuario {

    private List<Tiquete> tiquetes = new ArrayList<>();
    
    public Cliente(String usuario, long pin) {
        super(usuario, pin);
    }

    @Override
    public String toString() {
        return "Cliente{" + "tiquetes=" + tiquetes + '}';
    }

    public List<Tiquete> getTiquetes() {
        return tiquetes;
    }

    public void setTiquetes(List<Tiquete> tiquetes) {
        this.tiquetes = tiquetes;
    }

}
