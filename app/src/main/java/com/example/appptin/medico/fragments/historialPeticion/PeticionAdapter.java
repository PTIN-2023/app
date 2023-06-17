package com.example.appptin.medico.fragments.historialPeticion;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appptin.medico.conexion.InformacionBase;

import java.util.ArrayList;
import com.example.appptin.R;
import com.example.appptin.medico.fragments.aprobarPeticion.AprobarFragment;
import com.example.appptin.medico.fragments.detallesPeticion.DetallesPeticionFragment;
import com.example.appptin.medico.fragments.historialPaciente.HistorialPacienteFragment;
import com.example.appptin.medico.fragments.historialPaciente.InformePaciente;
import com.example.appptin.medico.fragments.perfilmedico.opciones.ConfigMedicoFragment;

public class PeticionAdapter extends RecyclerView.Adapter<PeticionAdapter.MyHolder> {
    Context context;
    FragmentManager activity;
    //ArrayList<PeticionClass> arrayList;
    //ArrayList<InformacionBase> arrayList;

    ArrayList<InformacionPeticion> arrayList;

    LayoutInflater layoutInflater;
    int codi;

    //Constructor
    public PeticionAdapter(Context context, ArrayList<InformacionPeticion> arrayList, int codigo, FragmentManager activity) {
        this.context = context;
        this.activity = activity;
        this.arrayList = arrayList;
        this.codi = codigo;
        layoutInflater = LayoutInflater.from(context); //Obtener el contexto del activity
    }


    // Crear el objeto "View" a partir del diseño item_file.xml (representa el diseño xml)
    // Devuelve la instáncia de la clase MyHolder
    // El layoutManager invoca este método para redenrizar cada elemento del RecyclerView
    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.contenedor_cliente, parent, false);

        return new MyHolder(view);
    }

    // Asignar valores para cada elemento de la lista
    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        holder.full_name.setText(arrayList.get(position).getPatient_fullname());
        holder.fecha_peticion.setText(arrayList.get(position).getDate());

        // Evento sobre cada Elemento

        holder.contenedorElem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String full_name = arrayList.get(position).getPatient_fullname();
                String fecha_peticion = arrayList.get(position).getDate();

                //Toast.makeText(context,"HOLAAAAAAA!",Toast.LENGTH_SHORT).show();

                //creo el bundle que contendrá el texto a pasar a los fragments
                Bundle result = new Bundle();
                Fragment cambiofragment = null;

                // carga la ventana del fragment según el Nº de código
                // Lista de peticiones pendientes por confirmar
                if (codi == 1){
                    result.putSerializable("MiObjeto", (InformacionPeticion) arrayList.get(position));
                    activity.setFragmentResult("key_aprobar_peticion", result);

                    cambiofragment = new AprobarFragment(activity,context);
                }
                // Historico de las peticiones
                else if (codi == 2){
                    result.putSerializable("MiObjeto", (InformacionPeticion) arrayList.get(position));
                    activity.setFragmentResult("key_aprobar_peticion", result);

                    cambiofragment = new DetallesPeticionFragment(activity,context);
                }
                // Historial Paciente
                else if (codi == 3){
                    result.putSerializable("MiObjeto", (InformacionPeticion) arrayList.get(position));
                    activity.setFragmentResult("key_aprobar_peticion", result);

                    cambiofragment = new HistorialPacienteFragment();
                }

                FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                //Cambio de Fragment
                fragmentTransaction.replace(R.id.frame_container, cambiofragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

    }

    // Devuelve la cantidad de elementos del ReclyclerView
    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    // Obtener referencias de los componentes visuales para cada elemento
    // Es decir, referencias de los TextViews correspondientes al DNI y Nombre
    public class MyHolder extends RecyclerView.ViewHolder {
        //TextView dni, nombre, apellido;
        TextView full_name, fecha_peticion;
        View contenedorElem;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            /*dni = itemView.findViewById(R.id.txt_dni);
            nombre = itemView.findViewById(R.id.txt_nombre);
            apellido = itemView.findViewById(R.id.txt_apellido);
             */
            full_name = itemView.findViewById(R.id.txt_full_name);
            fecha_peticion = itemView.findViewById(R.id.txt_fecha_peticion);


            contenedorElem = itemView.findViewById(R.id.layout_elementos);
        }
    }


}





