package com.example.appptin.medico.fragments.recetaPaciente;
import com.example.appptin.medico.conexion.InformacionBase;


import java.io.Serializable;

public class InformePaciente extends InformacionBase implements Serializable {
    //private String id,medico,nombre,apellidos,dni,fecha,genero,cip,pais,provincia,antecedente,problema,tratamiento;
    private String medico, fecha, genero,cip,pais,provincia,antecedente,problema,tratamiento;

    //Constructor
    public InformePaciente(String id, String nombre, String apellidos, String dni, String medico, String fecha, String genero,
                           String cip, String pais, String provincia, String antecedente, String problema, String tratamiento) {
        super(id, nombre, apellidos, dni);
        this.medico = medico;
        this.fecha = fecha;
        this.genero = genero;
        this.cip = cip;
        this.pais = pais;
        this.provincia = provincia;
        this.antecedente = antecedente;
        this.problema = problema;
        this.tratamiento = tratamiento;
    }

    // Setters
    public void setMedico(String medico) {this.medico = medico;}

    public void setFecha(String fecha) {this.fecha = fecha;}

    public void setGenero(String genero) {this.genero = genero;}

    public void setCip(String cip) {this.cip = cip;}

    public void setPais(String pais) {this.pais = pais;}

    public void setProvincia(String provincia) {this.provincia = provincia;}

    public void setAntecedente(String antecedente) {this.antecedente = antecedente;}

    public void setProblema(String problema) {this.problema = problema;}

    public void setTratamiento(String tratamiento) {this.tratamiento = tratamiento;}

    // Getters
    public String getMedico() {return medico;}

    public String getFecha() {return fecha;}

    public String getGenero() {return genero;}

    public String getCip() {return cip;}

    public String getPais() {return pais;}

    public String getProvincia() {return provincia;}

    public String getAntecedente() {return antecedente;}

    public String getProblema() {return problema;}

    public String getTratamiento() {return tratamiento;}

    @Override
    public String toString() {
        return "InformePaciente{" +
                "medico='" + medico + '\'' +
                ", fecha='" + fecha + '\'' +
                ", genero='" + genero + '\'' +
                ", cip='" + cip + '\'' +
                ", pais='" + pais + '\'' +
                ", provincia='" + provincia + '\'' +
                ", antecedente='" + antecedente + '\'' +
                ", problema='" + problema + '\'' +
                ", tratamiento='" + tratamiento + '\'' +
                '}';
    }
}
