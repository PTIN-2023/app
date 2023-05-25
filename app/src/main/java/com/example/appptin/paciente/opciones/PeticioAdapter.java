package com.example.appptin.paciente.opciones;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appptin.Peticio;
import com.example.appptin.R;

import java.util.ArrayList;

public class PeticioAdapter extends RecyclerView.Adapter<PeticioAdapter.PeticioViewHolder> {

private ArrayList<Peticio> peticions;

public PeticioAdapter(ArrayList<Peticio> peticions) {
        this.peticions = peticions;
        }

@NonNull
@Override
public PeticioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.peticio_item, parent, false);
        return new PeticioViewHolder(view);
        }

@Override
public void onBindViewHolder(@NonNull PeticioViewHolder holder, int position) {
        Peticio peticio = peticions.get(position);
        holder.bind(peticio);
        }

@Override
public int getItemCount() {
        return peticions.size();
        }

public class PeticioViewHolder extends RecyclerView.ViewHolder {

    private TextView txtNom;
    private TextView txtDescripcio;

    public PeticioViewHolder(@NonNull View itemView) {
        super(itemView);
        txtNom = itemView.findViewById(R.id.med_Nom);
        //txtDescripcio = itemView.findViewById(R.id.txtDescripcio);
    }

    public void bind(Peticio medicament) {
        //txtNom.setText(medicament.getNom());
        //txtDescripcio.setText(medicament.getDescripcio());
    }
}
}
