/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package api.models;

/**
 *
 * @author Juan José Molano Franco
 */
public class Tiquete {
    
    private Funcion funcion;
    private int asiento;

    public Tiquete(Funcion funcion, int asiento) {
        this.funcion = funcion;
        this.asiento = asiento;
    }

    @Override
    public String toString() {
        return "Tiquete{" + "funcion=" + funcion + ", asiento=" + asiento + '}';
    }

    public Funcion getFuncion() {
        return funcion;
    }

    public void setFuncion(Funcion funcion) {
        this.funcion = funcion;
    }

    public int getAsiento() {
        return asiento;
    }

    public void setAsiento(int asiento) {
        this.asiento = asiento;
    }
}
