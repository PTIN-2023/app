package com.example.appptin.gestor.fragments.flota.local.drons;

import org.json.JSONObject;

import java.io.Serializable;

public class InformacionDron implements Serializable {
    private String nombre_dron;
    private int estado;

    public InformacionDron(int identificador, String nombre_dron, String estat, String bateria, String ultim_manteniment, String id_order, int estado, JSONObject punt_inici, JSONObject punt_desti, JSONObject locationAct) {
        this.nombre_dron = ("DRON "+ Integer.toString(identificador));
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
