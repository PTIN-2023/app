package com.example.appptin.gestor.fragments.flota.local.drons;

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
import com.example.appptin.gestor.fragments.flota.global.CocheAdapter;
import com.example.appptin.gestor.fragments.flota.global.accion.CocheAccionFragment;
import com.example.appptin.gestor.fragments.flota.local.drons.accion.DronAccionFragment;

import java.util.ArrayList;

public class DronAdapter extends RecyclerView.Adapter<DronAdapter.MyHolder>{
    ArrayList<InformacionDron> arrayList;
    LayoutInflater layoutInflater;
    Context context;
    FragmentManager activity;

    public DronAdapter(Context context, ArrayList<InformacionDron> arrayList, FragmentManager activity) {
        this.arrayList = arrayList;
        this.context = context;
        this.activity = activity;
        layoutInflater = LayoutInflater.from(context); //Obtener el contexto del activity
    }

    @NonNull
    @Override
    public DronAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.contenedor_drones, parent, false);
        return new DronAdapter.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DronAdapter.MyHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.txt_nombre.setText(arrayList.get(position).getNombre_dron());

        //Tratar los colores para el estado
        int colorRGreen;
        int color;
        if(arrayList.get(position).getEstado() == 1){
            colorRGreen = R.color.green_300;
            color = ContextCompat.getColor(context, colorRGreen);
            holder.view_estado.setBackgroundColor(color);
        } else if (arrayList.get(position).getEstado() == 2) {
            colorRGreen = R.color.red_300;
            color = ContextCompat.getColor(context, colorRGreen);
            holder.view_estado.setBackgroundColor(color);
        }

        holder.contenedorElem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DronAccionFragment dronAccionFragment = new DronAccionFragment();


                //Enviar datos a CocheAccionFragment
                Bundle result = new Bundle();
                result.putSerializable("Info_dron", arrayList.get(position));
                activity.setFragmentResult("key_info_dron", result);



                // Abrir ventana --> CocheAccionFragment
                FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame_container, dronAccionFragment);
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
            txt_nombre = itemView.findViewById(R.id.txt_contenedor_nombre_dron);
            view_estado = itemView.findViewById(R.id.roundView_contenedor_dron);
            contenedorElem = itemView.findViewById(R.id.layout_elementos_drones);
        }
    }
}
