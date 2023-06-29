package com.example.appptin.gestor.fragments.flota.global;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;


import com.example.appptin.R;
import com.example.appptin.gestor.fragments.flota.global.accion.CocheAccionFragment;


import java.util.ArrayList;

public class CocheAdapter extends RecyclerView.Adapter<CocheAdapter.MyHolder>{

    ArrayList<InformacionCoche> arrayList;
    LayoutInflater layoutInflater;
    Context context;
    FragmentManager activity;

    public CocheAdapter(Context context, ArrayList<InformacionCoche> arrayList, FragmentManager activity) {
        this.arrayList = arrayList;
        this.context = context;
        this.activity = activity;
        layoutInflater = LayoutInflater.from(context); //Obtener el contexto del activity
    }

    @NonNull
    @Override
    public CocheAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.contenedor_coche, parent, false);
        return new CocheAdapter.MyHolder(view);
    }

    @SuppressLint({"ResourceAsColor", "RecyclerView"})
    @Override
    public void onBindViewHolder(@NonNull CocheAdapter.MyHolder holder, int position) {
        holder.txt_nombre.setText(arrayList.get(position).getCoche());

        //Tratar los colores para el estado
        int colorRGreen;
        int color;
        if(arrayList.get(position).getEstat() == "waits"){
            colorRGreen = R.color.green_300;
            color = ContextCompat.getColor(context, colorRGreen);
            holder.view_estado.setBackgroundColor(color);
        } else if (arrayList.get(position).getEstat() != "waits") {
            colorRGreen = R.color.red_300;
            color = ContextCompat.getColor(context, colorRGreen);
            holder.view_estado.setBackgroundColor(color);
        }
        holder.contenedorElem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CocheAccionFragment cocheAccionFragment = new CocheAccionFragment();

                //Enviar datos a CocheAccionFragment
                Bundle result = new Bundle();
                result.putSerializable("Info_coche", arrayList.get(position));
                activity.setFragmentResult("key_info_coche", result);

                // Abrir ventana --> CocheAccionFragment
                FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame_container, cocheAccionFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView txt_nombre;
        View view_estado,contenedorElem;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            txt_nombre = itemView.findViewById(R.id.txt_contenedor_nombre);
            view_estado = itemView.findViewById(R.id.roundView_contenedor);
            contenedorElem = itemView.findViewById(R.id.layout_elementos_vehiculos);
        }
    }
}
