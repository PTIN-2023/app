package com.example.appptin;

import java.util.ArrayList;

public class Medicament {
    private String medName;
    private String nationalCode;
    private String useType;
    private String typeOfAdministration;
    private boolean prescriptionNeeded;
    private double pvp;
    private String form;
    private ArrayList<String> excipients;

    public Medicament(String medName, String nationalCode, String useType, String typeOfAdministration, boolean prescriptionNeeded, double pvp, String form, ArrayList<String> excipients) {
        this.medName = medName;
        this.nationalCode = nationalCode;
        this.useType = useType;
        this.typeOfAdministration = typeOfAdministration;
        this.prescriptionNeeded = prescriptionNeeded;
        this.pvp = pvp;
        this.form = form;
        this.excipients = excipients;
    }

    public String getMedName() {
        return medName;
    }

    public String getNationalCode() {
        return nationalCode;
    }

    public String getUseType() {
        return useType;
    }

    public String getTypeOfAdministration() {
        return typeOfAdministration;
    }

    public boolean isPrescriptionNeeded() {
        return prescriptionNeeded;
    }

    public double getPvp() {
        return pvp;
    }

    public String getForm() {
        return form;
    }

    public ArrayList<String> getExcipients() {
        return excipients;
    }

    public void setMedName(String medName) {
        this.medName = medName;
    }

    public void setNationalCode(String nationalCode) {
        this.nationalCode = nationalCode;
    }

    public void setUseType(String useType) {
        this.useType = useType;
    }

    public void setTypeOfAdministration(String typeOfAdministration) {
        this.typeOfAdministration = typeOfAdministration;
    }

    public void setPrescriptionNeeded(boolean prescriptionNeeded) {
        this.prescriptionNeeded = prescriptionNeeded;
    }

    public void setPvp(double pvp) {
        this.pvp = pvp;
    }

    public void setForm(String form) {
        this.form = form;
    }

    public void setExcipients(String excipients) {
        this.excipients.add(excipients);
    }
}
