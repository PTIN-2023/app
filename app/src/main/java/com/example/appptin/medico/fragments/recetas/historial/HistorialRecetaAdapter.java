package com.example.appptin.medico.fragments.recetas.historial;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
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
import com.example.appptin.gestor.fragments.flota.global.CocheAdapter;
import com.example.appptin.gestor.fragments.flota.global.InformacionCoche;
import com.example.appptin.gestor.fragments.flota.global.accion.CocheAccionFragment;
import com.example.appptin.medico.fragments.historialPeticion.InformacionPeticion;
import com.example.appptin.medico.fragments.historialPeticion.PeticionAdapter;
import com.example.appptin.medico.fragments.recetas.crear.InformePaciente;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class HistorialRecetaAdapter extends RecyclerView.Adapter<HistorialRecetaAdapter.MyHolder>{

    Context context;
    FragmentManager activity;

    ArrayList<InformacionPreinscripciones> arrayList;

    LayoutInflater layoutInflater;

    //Constructor
    public HistorialRecetaAdapter(Context context, ArrayList<InformacionPreinscripciones> arrayList, FragmentManager activity) {
        this.arrayList = arrayList;
        this.context = context;
        this.activity = activity;
        layoutInflater = LayoutInflater.from(context); //Obtener el contexto del activity
    }

    @NonNull
    @Override
    public HistorialRecetaAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.contenedor_receta_historial, parent, false);
        return new HistorialRecetaAdapter.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistorialRecetaAdapter.MyHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.fecha_uso_receta.setText("Recepta "+arrayList.get(position).getCont());

        holder.contenedorElem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                holder.showPrescriptionDialog(arrayList.get(position));

            }
        });

    }
    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView fecha_uso_receta;
        View contenedorElem;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            fecha_uso_receta = itemView.findViewById(R.id.txt_fecha_uso);


            contenedorElem = itemView.findViewById(R.id.layout_elementos_receta);
        }

        private void showPrescriptionDialog(InformacionPreinscripciones prescription) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Información de la receta");

            // Construir el mensaje con la información de la receta
            StringBuilder message = new StringBuilder();
            message.append("Duración: ").append(prescription.getDuration()).append("\n");
            message.append("Última vez utilizada: ").append(prescription.getLastUsed()).append("\n");
            message.append("Notas: ").append(prescription.getNotes()).append("\n");
            message.append("Identificador de la receta: ").append(prescription.getPrescriptionIdentifier()).append("\n");
            message.append("Renovación: ").append(prescription.getRenewal()).append("\n");
            message.append("Lista de medicamentos: ").append("\n");


            JSONArray medicineList = prescription.getMedicineList();
            for (int i = 0; i < medicineList.length(); i++) {
                try {
                    JSONArray medicine = medicineList.getJSONArray(i);
                    String nationalCode = medicine.getString(0);
                    String quantity = medicine.getString(1);
                    message.append("- Código Nacional: ").append(nationalCode).append(", Cantidad: ").append(quantity).append("\n");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            builder.setMessage(message.toString());

            builder.setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            builder.show();
        }
    }


}
