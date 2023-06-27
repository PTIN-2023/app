package com.example.appptin.gestor.fragments.flota.global;


import java.io.Serializable;

// ESTA CLASE CONTIENE TODOS LOS DATOS DEL COCHE
public class InformacionCoche implements Serializable {
    private String coche;
    private int estado;

    public InformacionCoche(String coche, int estado) {
        this.coche = coche;
        this.estado = estado;
    }

    public String getCoche() {
        return coche;
    }
    public int getEstado() {
        return estado;
    }

    public void setCoche(String coche) {
        this.coche = coche;
    }


    public void setEstado(int estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "InformacionCoche{" +
                "coche='" + coche + '\'' +
                ", estado=" + estado +
                '}';
    }
}
