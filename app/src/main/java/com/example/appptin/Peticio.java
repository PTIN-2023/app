package com.example.appptin;

import org.json.JSONObject;

import java.util.ArrayList;

public class Peticio {
    private int ID;
    //private String medName;
    //private String typeOfAdministration;
    //private String form;
    private String date;
    private String state;
    //private boolean prescriptionNeeded;
    private ArrayList<JSONObject> medicineList;

    public Peticio(int ID, String date, String state, ArrayList<JSONObject> medicineList) {
        this.ID = ID;
        //this.medName = medName;
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

    /*public String getMedName() {
        return medName;
    }*/

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

    public ArrayList<JSONObject> getMedicines() {
        return medicineList;
    }

    /*public String getExcipientsList() {
        String lista="";

        for (JSONObject element : getExcipients()) {
            lista += "  * "+ element +"\n";
        }
        return lista;
    }*/

}
