package com.example.appptin;

import java.util.ArrayList;

public class Peticio {
    private double ID;
    private String email_pacient;
    private String aprovat;
    private String reason;
    private String data;
    private String state;
    private ArrayList<Double> medsComanda;

    public Peticio(double ID, String email_pacient, String aprovat, String reason, String data, String state, ArrayList<Double> medsComanda) {
        this.ID = ID;
        this.email_pacient = email_pacient;
        this.aprovat = aprovat;
        this.reason = reason;
        this.data = data;
        this.state = state;
        this.medsComanda = medsComanda;
    }

    public double getID() {
        return ID;
    }

    public String getEmail_pacient() {
        return email_pacient;
    }

    public String getAprovat() {
        return aprovat;
    }

    public String getReason() {
        return reason;
    }

    public String getData() {
        return data;
    }

    public String getState() {
        return state;
    }

    public ArrayList<Double> getmedsComanda() {
        return medsComanda;
    }

    public String getmedsComandaList() {
        String lista="";

        for (Double element : getmedsComanda()) {
            lista += "  * "+ element +"\n";
        }
        return lista;
    }

}
