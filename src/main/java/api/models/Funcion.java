/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package api.models;

import java.util.List;

/**
 *
 * @author Juan José Molano Franco
 */
public class Funcion {
    
    private String inicio;
    private String fin;
    private boolean[] asientos;
    private String titulo;
    private int id;

    public Funcion(String inicio, String fin, String titulo, int asientos, int id) {
        this.inicio = inicio;
        this.fin = fin;
        this.titulo = titulo;
        this.asientos = new boolean[asientos];
        this.id = id;
    }

    @Override
    public String toString() {
        return titulo;
    }
    

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getInicio() {
        return inicio;
    }

    public void setInicio(String inicio) {
        this.inicio = inicio;
    }

    public String getFin() {
        return fin;
    }

    public void setFin(String fin) {
        this.fin = fin;
    }

    public boolean[] getAsientos() {
        return asientos;
    }

    public void setAsientos(boolean[] asientos) {
        this.asientos = asientos;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}

