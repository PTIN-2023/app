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

    @Override
    public String toString() {
        return "InformacionCiudadEdge{" +
                "ciudad='" + ciudad + '\'' +
                '}';
    }

    public int getNumEdge() {
        if (ciudad == "Barcelona") return 0;
        else if (ciudad == "Cubelles") return 1;
        else if (ciudad == "Vilanova") return 2;
        return 0;
    }

}
