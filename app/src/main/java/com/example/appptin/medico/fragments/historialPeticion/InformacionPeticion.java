package com.example.appptin.medico.fragments.historialPeticion;

import com.example.appptin.Medicament;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class InformacionPeticion implements Serializable {
    private String order_identifier, date,patient_fullname, approved;

    private Map<String, Medicament> medicine_list;

    public InformacionPeticion(String order_identifier, String date, String patient_fullname) {
        this.order_identifier = order_identifier;
        this.date = date;
        this.patient_fullname = patient_fullname;

        medicine_list = new HashMap<>();
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

    public void setMedicine_list(Map<String, Medicament> medicine_list) {
        this.medicine_list = medicine_list;
    }

    //La clave es el código nacional del medicamento y el valor el medicamento
    public void setMedicine_list(Medicament medicament){
        boolean existe_med = medicine_list.containsKey(medicament.getNationalCode());
        //Si ya existe medicamento, augmentar la cantidad
        if (existe_med){
            medicament.setCantidad(1);
        }

        medicine_list.put(medicament.getNationalCode(),medicament);
    }


    // GETTERS
    public String getOrder_identifier() {
        return order_identifier;
    }

    public String getDate() {
        return date;
    }

    public String getApproved(){return approved;}

    public String getPatient_fullname() {
        return patient_fullname;
    }

    public Map<String, Medicament> getMedicine_list() {
        return medicine_list;
    }

    public Medicament getMedicine_list(String clave) {
        return medicine_list.get(clave);
    }

    @Override
    public String toString() {
        return "InformacionPeticion{" +
                "order_identifier='" + order_identifier + '\'' +
                ", date='" + date + '\'' +
                ", patient_fullname='" + patient_fullname + '\'' +
                ", medicine_list=" + medicine_list +
                '}';
    }
}
