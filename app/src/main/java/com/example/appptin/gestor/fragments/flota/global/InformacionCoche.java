package com.example.appptin.gestor.fragments.flota.global;


import org.json.JSONArray;

import java.io.Serializable;

// ESTA CLASE CONTIENE TODOS LOS DATOS DEL COCHE
public class InformacionCoche implements Serializable {
    private int identificador;
    private String matricula, estat, bateria, ultim_manteniment;
    private JSONArray paquets;
    private double latitude_inici,longitude_inici,latitude_desti,longitude_desti,latitude,longitude;

    public InformacionCoche(int identificador, String matricula, String estat, String bateria, String ultim_manteniment, JSONArray paquets, double latitude_inici, double longitude_inici, double latitude_desti, double longitude_desti, double latitude, double longitude) {
        this.identificador = identificador;
        this.matricula = matricula;
        this.estat = estat;
        this.bateria = bateria;
        this.ultim_manteniment = ultim_manteniment;
        this.paquets = paquets;
        this.latitude_inici = latitude_inici;
        this.longitude_inici = longitude_inici;
        this.latitude_desti = latitude_desti;
        this.longitude_desti = longitude_desti;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // GETTERS


    public String getCoche() {
        return String.valueOf(identificador) + " - " + matricula;
    }

    public int getIdentificador() {
        return identificador;
    }

    public String getMatricula() {
        return matricula;
    }

    public String getEstat() {
        return estat;
    }

    public String getBateria() {
        return bateria;
    }

    public String getUltim_manteniment() {
        return ultim_manteniment;
    }

    public JSONArray getPaquets() {
        return paquets;
    }

    public double getLatitude_inici() {
        return latitude_inici;
    }

    public double getLongitude_inici() {
        return longitude_inici;
    }

    public double getLatitude_desti() {
        return latitude_desti;
    }

    public double getLongitude_desti() {
        return longitude_desti;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    // SETTERS

    public void setIdentificador(int identificador) {
        this.identificador = identificador;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public void setEstat(String estat) {
        this.estat = estat;
    }

    public void setBateria(String bateria) {
        this.bateria = bateria;
    }

    public void setUltim_manteniment(String ultim_manteniment) {
        this.ultim_manteniment = ultim_manteniment;
    }

    public void setPaquets(JSONArray paquets) {
        this.paquets = paquets;
    }

    public void setLatitude_inici(double latitude_inici) {
        this.latitude_inici = latitude_inici;
    }

    public void setLongitude_inici(double longitude_inici) {
        this.longitude_inici = longitude_inici;
    }

    public void setLatitude_desti(double latitude_desti) {
        this.latitude_desti = latitude_desti;
    }

    public void setLongitude_desti(double longitude_desti) {
        this.longitude_desti = longitude_desti;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "InformacionCoche{" +
                "matricula='" + matricula + '\'' +
                ", estat='" + estat + '\'' +
                ", bateria='" + bateria + '\'' +
                ", ultim_manteniment='" + ultim_manteniment + '\'' +
                ", paquets=" + paquets +
                ", latitude_inici=" + latitude_inici +
                ", longitude_inici=" + longitude_inici +
                ", latitude_desti=" + latitude_desti +
                ", longitude_desti=" + longitude_desti +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
