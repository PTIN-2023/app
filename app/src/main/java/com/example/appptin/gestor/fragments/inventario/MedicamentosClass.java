package com.example.appptin.gestor.fragments.inventario;

import java.io.Serializable;

public class MedicamentosClass implements Serializable  {
    private String nombre_medicamento,color;


    public MedicamentosClass(String nombre_medicamento, String color) {
        this.nombre_medicamento = nombre_medicamento;
        this.color = color;
    }
    public String getNombre_medicamento() {
        return nombre_medicamento;
    }
    public void setNombre_medicamento(String nombre_medicamento) {
        this.nombre_medicamento = nombre_medicamento;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}



