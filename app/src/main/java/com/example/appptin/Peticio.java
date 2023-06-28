package com.example.appptin;

import org.json.JSONArray;

import java.util.ArrayList;

public class Peticio {
    private int ID;
    private String email;
    //private String typeOfAdministration;
    //private String form;
    private String date;
    private String state;
    //private boolean prescriptionNeeded;
    private ArrayList<JSONArray> medicineList;

    public Peticio(String email, int ID, String date, String state, JSONArray medicineList) {
        this.ID = ID;
        this.email = email;
        //this.typeOfAdministration = typeOfAdministration;
        //this.form = form;
        this.date = date;
        this.state = state;
        //this.prescriptionNeeded = prescriptionNeeded;
        this.medicineList = medicineList;
    }

    public int getID() {
        return ID;
    }

    public String getEmail() {
        return email;
    }

    /*public String getTypeOfAdministration() {
        return typeOfAdministration;
    }*/

    /*public String getForm() {
        return form;
    }*/

    public String getDate() {
        return date;
    }

    public String getState() {
        return state;
    }
    /*public boolean getprescriptionNeeded() {
        return prescriptionNeeded;
    }*/

    public ArrayList<JSONArray> getMedicines() {
        return medicineList;
    }

    public void setState(String state) {
        this.state = state;
    }

}
