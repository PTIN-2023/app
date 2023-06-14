package com.example.appptin.medico.fragments.historialPeticion;

import com.example.appptin.Medicament;

import java.io.Serializable;
import java.util.ArrayList;

public class InformacionPeticion implements Serializable {
    private String order_identifier, date,patient_fullname;
    private ArrayList<Medicament> medicine_list;

    public InformacionPeticion(String order_identifier, String date, String patient_fullname, ArrayList<Medicament> medicine_list) {
        this.order_identifier = order_identifier;
        this.date = date;
        this.patient_fullname = patient_fullname;
        this.medicine_list = medicine_list;
    }

    // SETTERS
    public void setOrder_identifier(String order_identifier) {
        this.order_identifier = order_identifier;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setPatient_fullname(String patient_fullname) {
        this.patient_fullname = patient_fullname;
    }

    public void setMedicine_list(ArrayList<Medicament> medicine_list) {
        this.medicine_list = medicine_list;
    }

    // GETTERS

    public String getOrder_identifier() {
        return order_identifier;
    }

    public String getDate() {
        return date;
    }

    public String getPatient_fullname() {
        return patient_fullname;
    }

    public ArrayList<Medicament> getMedicine_list() {
        return medicine_list;
    }
}
