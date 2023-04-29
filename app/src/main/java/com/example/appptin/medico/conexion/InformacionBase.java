package com.example.appptin.medico.conexion;

public class InformacionBase {

    private String id,nombre,apellidos,dni;

    public InformacionBase(){}
    public InformacionBase(String id, String nombre, String apellidos, String dni) {
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.dni = dni;
    }

    // Getters
    public String getId() {return id;}
    public String getDni() {return dni;}
    public String getNombre() {
        return nombre;
    }
    public String getApellidos() {
        return apellidos;
    }


    // Setters
    public void setId(String id) { this.id = id;}
    public void setNombre(String nombre) {this.nombre = nombre;}
    public void setApellidos(String apellidos) {this.apellidos = apellidos;}
    public void setDni(String dni) {this.dni = dni;}

    @Override
    public String toString() {
        return "PeticionClass{" +
                "id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", dni='" + dni + '\'' +
                //", medicamentos=" + medicamentos +
                '}';
    }
}
