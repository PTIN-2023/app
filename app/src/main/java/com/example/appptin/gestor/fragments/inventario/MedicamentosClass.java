package com.example.appptin.gestor.fragments.inventario;

import java.io.Serializable;
import java.util.ArrayList;

public class MedicamentosClass implements Serializable  {
    private String medName;
    private String nationalCode;
    private String useType;
    private String typeOfAdministration;
    private boolean prescriptionNeeded;
    private Double pvp;
    private String form;
    private ArrayList<String> excipients;

    private int cantidad = 0;

    private String URLimage;

    public MedicamentosClass(String medName, String URLimage, String nationalCode, String useType,
                             String typeOfAdministration, boolean prescriptionNeeded,
                             Double pvp, String form, ArrayList<String> excipients) {
        this.medName = medName;
        this.URLimage = URLimage;
        this.nationalCode = nationalCode;
        this.useType = useType;
        this.typeOfAdministration = typeOfAdministration;
        this.prescriptionNeeded = prescriptionNeeded;
        this.pvp = pvp;
        this.form = form;
        this.excipients = excipients;
    }

    public MedicamentosClass(String medName, String URLimage, String nationalCode, String useType,
                             String typeOfAdministration, boolean prescriptionNeeded,
                             Double pvp, int cantidad, String form, ArrayList<String> excipients) {
        this.medName = medName;
        this.URLimage = URLimage;
        this.nationalCode = nationalCode;
        this.useType = useType;
        this.typeOfAdministration = typeOfAdministration;
        this.prescriptionNeeded = prescriptionNeeded;
        this.pvp = pvp;
        this.cantidad = cantidad;
        this.form = form;
        this.excipients = excipients;
    }

    public String getNombre_medicamento() {
        return medName;
    }

    public String getCodiNacional_medicamento() {
        return nationalCode;
    }

    public String getUseType_medicamento() {
        return useType;
    }

    public String getAdministration_medicamento() {
        return typeOfAdministration;
    }

    public String getPrescription_medicamento() {
        if (prescriptionNeeded) return "Si";
        else return "No";
    }

    public String getPvP_medicamento() {
        return Double.toString(pvp);
    }

    public int getQuantitat_medicamento(){
        return cantidad;
    }

    public String getForm_medicamento() {
        return form;
    }

    public String getURLimage(){return URLimage;}

    public void setNombre_medicamento(String nombre_medicamento) {
        this.medName = nombre_medicamento;
    }

}



