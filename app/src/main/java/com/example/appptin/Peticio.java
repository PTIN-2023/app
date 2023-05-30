package com.example.appptin;

import java.util.ArrayList;

public class Peticio {
    private double ID;
    private String medName;
    private String typeOfAdministration;
    private String form;
    private String date;
    private String state;
    private boolean prescriptionNeeded;
    private ArrayList<String> excipients;

    public Peticio(double ID, String medName, String typeOfAdministration, String form, String date, String state, boolean prescriptionNeeded, ArrayList<String> excipients) {
        this.ID = ID;
        this.medName = medName;
        this.typeOfAdministration = typeOfAdministration;
        this.form = form;
        this.date = date;
        this.state = state;
        this.prescriptionNeeded = prescriptionNeeded;
        this.excipients = excipients;
    }

    public double getID() {
        return ID;
    }

    public String getMedName() {
        return medName;
    }

    public String getTypeOfAdministration() {
        return typeOfAdministration;
    }

    public String getForm() {
        return form;
    }

    public String getDate() {
        return date;
    }

    public String getState() {
        return state;
    }
    public boolean getprescriptionNeeded() {
        return prescriptionNeeded;
    }

    public ArrayList<String> getExcipients() {
        return excipients;
    }

    public String getExcipientsList() {
        String lista="";

        for (String element : getExcipients()) {
            lista += "  * "+ element +"\n";
        }
        return lista;
    }

}
