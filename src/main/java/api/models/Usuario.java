/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package api.models;

/**
 *
 * @author Juan José Molano Franco
 */
public class Usuario {
    
    private String usuario;
    private long pin;

    public Usuario(String usuario, long pin) {
        this.usuario = usuario;
        this.pin = pin;
    }

    @Override
    public String toString() {
        return "Usuario{" + "usuario=" + usuario + ", pin=" + pin + '}';
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public long getPin() {
        return pin;
    }

    public void setPin(long pin) {
        this.pin = pin;
    }


}
