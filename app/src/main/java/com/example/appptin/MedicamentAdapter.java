package com.example.appptin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MedicamentAdapter extends RecyclerView.Adapter<MedicamentAdapter.MedicamentViewHolder> {

    private ArrayList<Medicament> medicaments;

    public MedicamentAdapter(ArrayList<Medicament> medicaments) {
        this.medicaments = medicaments;
    }

    @NonNull
    @Override
    public MedicamentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.medicament_item, parent, false);
        return new MedicamentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicamentViewHolder holder, int position) {
        Medicament medicament = medicaments.get(position);
        holder.bind(medicament);
    }

    @Override
    public int getItemCount() {
        return medicaments.size();
    }

    public class MedicamentViewHolder extends RecyclerView.ViewHolder {

        private TextView txtNom;
        private TextView txtDescripcio;

        public MedicamentViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNom = itemView.findViewById(R.id.med_Nom);
            //txtDescripcio = itemView.findViewById(R.id.txtDescripcio);
        }

        public void bind(Medicament medicament) {
            //txtNom.setText(medicament.getNom());
            //txtDescripcio.setText(medicament.getDescripcio());
        }
    }
}
