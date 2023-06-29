package com.example.appptin.gestor.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appptin.R;
import com.example.appptin.gestor.fragments.EdgeMapaCiudadFragment;
import com.example.appptin.gestor.fragments.flota.local.EdgeCiudadAdapter;
import com.example.appptin.gestor.fragments.flota.local.InformacionCiudadEdge;
import com.example.appptin.gestor.fragments.flota.local.drons.DronFragment;
import com.example.appptin.gestor.fragments.inventario.MapaGestor;
import com.example.appptin.gestor.fragments.inventario.MapaGestorEdge;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.appptin.R;
import com.example.appptin.gestor.fragments.flota.global.CochesFragment;
import com.example.appptin.gestor.fragments.flota.local.EdgeCiudadFragment;
import com.example.appptin.gestor.fragments.inventario.MapaGestor;

import java.util.ArrayList;

public class EdgeMapaCiudadAdapter extends RecyclerView.Adapter<EdgeMapaCiudadAdapter.MyHolder>{


    private FragmentActivity fragmentActivity;
    ArrayList<InformacionCiudadEdge> arrayList;
    LayoutInflater layoutInflater;
    Context context;
    FragmentManager activity;

    public EdgeMapaCiudadAdapter(Context context, ArrayList<InformacionCiudadEdge> arrayList, FragmentManager activity) {
        this.arrayList = arrayList;
        this.context = context;
        this.activity = activity;
        layoutInflater = LayoutInflater.from(context); //Obtener el contexto del activity
    }

    public EdgeMapaCiudadAdapter(Context context, ArrayList<InformacionCiudadEdge> arrayList, FragmentManager activity, FragmentActivity fragmentActivity) {
        this.arrayList = arrayList;
        this.context = context;
        this.activity = activity;
        this.fragmentActivity = fragmentActivity; // Afegit el paràmetre fragmentActivity
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public EdgeMapaCiudadAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.contenedor_edge_ciudad, parent, false);
        return new EdgeMapaCiudadAdapter.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EdgeMapaCiudadAdapter.MyHolder holder, int position) {
        holder.txt_nombre.setText(arrayList.get(position).getCiudad());


        holder.contenedorElem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                int numEdge = arrayList.get(position).getNumEdge();
                Intent intent = new Intent(context, MapaGestorEdge.class);
                intent.putExtra("parametreInt", numEdge);
                context.startActivity(intent);
                    //DronFragment dronFragment = new DronFragment();



                //DronFragment dronFragment = new DronFragment();

                ///*
                ////Enviar datos a CocheAccionFragment
                //Bundle result = new Bundle();
                //result.putSerializable("Info_coche", arrayList.get(position));
                //activity.setFragmentResult("key_info_coche", result);
                // */
//
                //// Abrir ventana --> CocheAccionFragment
                //FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                //FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                //fragmentTransaction.replace(R.id.frame_container, dronFragment);
                //fragmentTransaction.addToBackStack(null);
                //fragmentTransaction.commit();

            }
        });


    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView txt_nombre;
        View contenedorElem;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            txt_nombre = itemView.findViewById(R.id.txt_contenedor_nombre_ciudad);;
            contenedorElem = itemView.findViewById(R.id.layout_elementos_ciudades);
        }
    }
}
