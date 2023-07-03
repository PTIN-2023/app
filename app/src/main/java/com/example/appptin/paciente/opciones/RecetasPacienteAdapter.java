package com.example.appptin.paciente.opciones;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appptin.R;


import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class RecetasPacienteAdapter extends RecyclerView.Adapter<RecetasPacienteAdapter.MyHolder>{

    Context context;
    FragmentManager activity;

    ArrayList<InformacionRecetaPAciente> arrayList;

    LayoutInflater layoutInflater;

    //Constructor
    public RecetasPacienteAdapter(Context context, ArrayList<InformacionRecetaPAciente> arrayList, FragmentManager activity) {
        this.arrayList = arrayList;
        this.context = context;
        this.activity = activity;
        layoutInflater = LayoutInflater.from(context); //Obtener el contexto del activity
    }

    @NonNull
    @Override
    public RecetasPacienteAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.contenedor_receta_historial, parent, false);
        return new RecetasPacienteAdapter.MyHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull RecetasPacienteAdapter.MyHolder holder, @SuppressLint("RecyclerView") int position) {
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

        private void showPrescriptionDialog(InformacionRecetaPAciente prescription) {
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
