package com.example.appptin.gestor.fragments.flota.local.drons;

import java.io.Serializable;

public class InformacionDron implements Serializable {
    private String nombre_dron;
    private int estado;

    public InformacionDron(String nombre_dron, int estado) {
        this.nombre_dron = nombre_dron;
        this.estado = estado;
    }

    public String getNombre_dron() {
        return nombre_dron;
    }
    public int getEstado() {
        return estado;
    }

    public void setNombre_dron(String coche) {
        this.nombre_dron = coche;
    }


    public void setEstado(int estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "InformacionCoche{" +
                "Dron='" + nombre_dron + '\'' +
                ", estado=" + estado +
                '}';
    }
}
