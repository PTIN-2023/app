package com.example.appptin.medico.fragments.recetas.historial;

import org.json.JSONArray;

import java.util.List;

public class InformacionPreinscripciones {

    private int cont;
    private String duration;
    private String lastUsed;
    private JSONArray medicineList;
    private String notes;
    private String prescriptionIdentifier;
    private String renewal;

    public InformacionPreinscripciones(){};

    public InformacionPreinscripciones(String duration, String lastUsed, JSONArray medicineList, String notes, String prescriptionIdentifier, String renewal,int cont) {
        this.duration = duration;
        this.lastUsed = lastUsed;
        this.medicineList = medicineList;
        this.notes = notes;
        this.prescriptionIdentifier = prescriptionIdentifier;
        this.renewal = renewal;
        this.cont = cont;
    }

    public int getCont() {
        return cont;
    }
    public void setCont(int cont) {
        this.cont = cont;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getLastUsed() {
        return lastUsed;
    }

    public void setLastUsed(String lastUsed) {
        this.lastUsed = lastUsed;
    }

    public JSONArray getMedicineList() {
        return medicineList;
    }

    public void setMedicineList(JSONArray medicineList) {
        this.medicineList = medicineList;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getPrescriptionIdentifier() {
        return prescriptionIdentifier;
    }

    public void setPrescriptionIdentifier(String prescriptionIdentifier) {
        this.prescriptionIdentifier = prescriptionIdentifier;
    }

    public String getRenewal() {
        return renewal;
    }

    public void setRenewal(String renewal) {
        this.renewal = renewal;
    }

    @Override
    public String toString() {
        return "InformacionPreinscripciones{" +
                "duration='" + duration + '\'' +
                ", lastUsed='" + lastUsed + '\'' +
                ", medicineList=" + medicineList +
                ", notes='" + notes + '\'' +
                ", prescriptionIdentifier='" + prescriptionIdentifier + '\'' +
                ", renewal='" + renewal + '\'' +
                '}';
    }
}
