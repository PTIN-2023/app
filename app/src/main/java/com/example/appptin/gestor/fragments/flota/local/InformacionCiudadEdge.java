package com.example.appptin.gestor.fragments.flota.local;

import java.io.Serializable;

public class InformacionCiudadEdge implements Serializable {
    private String ciudad;

    public InformacionCiudadEdge(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }
}
