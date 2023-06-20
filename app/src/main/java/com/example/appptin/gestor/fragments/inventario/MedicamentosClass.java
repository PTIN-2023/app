package com.example.appptin.gestor.fragments.inventario;

import java.io.Serializable;
import java.util.ArrayList;

public class MedicamentosClass implements Serializable  {
    private String medName;
    private String nationalCode;
    private String useType;
    private String typeOfAdministration;
    private boolean prescriptionNeeded;
    private double pvp;
    private String form;
    private ArrayList<String> excipients;

    private int cantidad = 0;


    public MedicamentosClass(String medName, String nationalCode, String useType,
                             String typeOfAdministration, boolean prescriptionNeeded,
                             double pvp, String form, ArrayList<String> excipients) {
        this.medName = medName;
        this.nationalCode = nationalCode;
        this.useType = useType;
        this.typeOfAdministration = typeOfAdministration;
        this.prescriptionNeeded = prescriptionNeeded;
        this.pvp = pvp;
        this.form = form;
        this.excipients = excipients;
    }
    public String getNombre_medicamento() {
        return medName;
    }
    public void setNombre_medicamento(String nombre_medicamento) {
        this.medName = nombre_medicamento;
    }

//    public String getColor() {
//        return color;
//    }

//    public void setColor(String color) {
//        this.color = color;
//    }
}



