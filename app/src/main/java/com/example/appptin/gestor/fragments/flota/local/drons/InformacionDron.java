package com.example.appptin.gestor.fragments.flota.local.drons;

import org.json.JSONObject;

import java.io.Serializable;

public class InformacionDron implements Serializable {
    private int identificador,id_beehive;
    private String autonomia,estat,bateria,ultim_manteniment,id_order;
    private JSONObject punt_inici, punt_desti, locationAct;

    public InformacionDron(int identificador, int id_beehive, String autonomia, String estat, String bateria, String ultim_manteniment,
                           String id_order, JSONObject punt_inici, JSONObject punt_desti, JSONObject locationAct) {
        this.identificador = identificador;
        this.id_beehive = id_beehive;
        this.autonomia = autonomia;
        this.estat = estat;
        this.bateria = bateria;
        this.ultim_manteniment = ultim_manteniment;
        this.id_order = id_order;
        this.punt_inici = punt_inici;
        this.punt_desti = punt_desti;
        this.locationAct = locationAct;
    }

    public String getNombre_dron() {
        return "Dron "+String.valueOf(identificador);
    }

    //GETTERS

    public int getIdentificador() {
        return identificador;
    }

    public int getId_beehive() {
        return id_beehive;
    }

    public String getAutonomia() {
        return autonomia;
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

    public String getId_order() {
        return id_order;
    }

    public JSONObject getPunt_inici() {
        return punt_inici;
    }

    public JSONObject getPunt_desti() {
        return punt_desti;
    }

    public JSONObject getLocationAct() {
        return locationAct;
    }

    //SETTER

    public void setIdentificador(int identificador) {
        this.identificador = identificador;
    }

    public void setId_beehive(int id_beehive) {
        this.id_beehive = id_beehive;
    }

    public void setAutonomia(String autonomia) {
        this.autonomia = autonomia;
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

    public void setId_order(String id_order) {
        this.id_order = id_order;
    }

    public void setPunt_inici(JSONObject punt_inici) {
        this.punt_inici = punt_inici;
    }

    public void setPunt_desti(JSONObject punt_desti) {
        this.punt_desti = punt_desti;
    }

    public void setLocationAct(JSONObject locationAct) {
        this.locationAct = locationAct;
    }

    @Override
    public String toString() {
        return "InformacionDron{" +
                "identificador=" + identificador +
                ", id_beehive=" + id_beehive +
                ", autonomia='" + autonomia + '\'' +
                ", estat='" + estat + '\'' +
                ", bateria='" + bateria + '\'' +
                ", ultim_manteniment='" + ultim_manteniment + '\'' +
                ", id_order='" + id_order + '\'' +
                ", punt_inici=" + punt_inici +
                ", punt_desti=" + punt_desti +
                ", locationAct=" + locationAct +
                '}';
    }
}
