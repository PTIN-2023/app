package com.example.appptin.gestor.fragments.inventario;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appptin.R;

import java.util.ArrayList;

public class MedicamentosAdapter extends RecyclerView.Adapter<MedicamentosAdapter.MyHolder> {
    Context context;
    FragmentManager activity;
    ArrayList<MedicamentosClass> arrayList;
    LayoutInflater layoutInflater;

    //Constructor
    public MedicamentosAdapter(Context context, ArrayList<MedicamentosClass> arrayList, FragmentManager activity) {
        this.context = context;
        this.activity = activity;
        this.arrayList = arrayList;
        layoutInflater = LayoutInflater.from(context); //Obtener el contexto del activity
    }

    // Crear el objeto "View" a partir del diseño item_file.xml (representa el diseño xml)
    // Devuelve la instáncia de la clase MyHolder
    // El layoutManager invoca este método para redenrizar cada elemento del RecyclerView
    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.medicamentos_inventario, parent, false);

        return new MyHolder(view);
    }

    // Asignar valores para cada elemento de la lista
    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        holder.txt_medicamento.setText(arrayList.get(position).getNombre_medicamento());

//        if(arrayList.get(position).getColor() == "red") holder.txt_medicamento.setTextColor(Color.RED);
//        else if (arrayList.get(position).getColor() == "green") holder.txt_medicamento.setTextColor(Color.GREEN);

        holder.contenedorElem.setOnClickListener(evento_contenedor);

    }

    private View.OnClickListener evento_contenedor = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            InventarioMedicamentoFragment inv_med_frag = new InventarioMedicamentoFragment();
            FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            //Cambio de Fragment
            fragmentTransaction.replace(R.id.frame_container, inv_med_frag);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    };

    // Devuelve la cantidad de elementos del ReclyclerView
    @Override
    public int getItemCount() {
        if (arrayList == null) {
            return 0;
        } else {
            return arrayList.size();
        }
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView txt_medicamento;
        View contenedorElem;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            //Estas referencias son del layout: medicamentos.inventario.xml
            txt_medicamento = itemView.findViewById(R.id.txt_medicamento_inventario);
            contenedorElem = itemView.findViewById(R.id.contenedor_medicamentos);
        }
    }
}






