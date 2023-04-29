package com.example.appptin.medico.fragments.historialPeticion;



import com.example.appptin.medico.conexion.InformacionBase;

import java.io.Serializable;
import java.util.ArrayList;

public class PeticionClass extends InformacionBase implements Serializable  {

    //private String id,nombre,apellidos,dni,numero_pedido;
    String numero_pedido;
    private ArrayList<String> medicamentos;

    public PeticionClass(String numero_pedido, ArrayList<String> medicamentos) {
        this.numero_pedido = numero_pedido;
        this.medicamentos = medicamentos;
    }

    public PeticionClass(String id, String nombre, String apellidos, String dni, String numero_pedido, ArrayList<String> medicamentos) {
        super(id, nombre, apellidos, dni);
        this.numero_pedido = numero_pedido;
        this.medicamentos = medicamentos;
    }
    public String getNumeroPedido() {return numero_pedido;}
    public ArrayList<String> getMedicamentos() {return medicamentos;}

    public int getMedicamentosSize(){return  medicamentos.size();}

    public void setMedicamentos(ArrayList<String> medicamentos) {this.medicamentos = new ArrayList<String>(medicamentos);}

    public void setMedicamentos(String medicamentos) {

        if (this.medicamentos == null) this.medicamentos = new ArrayList<String>();
        this.medicamentos.add(medicamentos);
    }
    public void setNumeroPedido(String numero_pedido) {this.numero_pedido = numero_pedido;}


}
