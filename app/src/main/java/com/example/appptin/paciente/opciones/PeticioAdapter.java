package com.example.appptin.paciente.opciones;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appptin.Medicament;
import com.example.appptin.Peticio;
import com.example.appptin.R;

import java.util.ArrayList;

public class PeticioAdapter extends RecyclerView.Adapter<PeticioAdapter.PeticioViewHolder> {


    private ArrayList<Peticio> peticions;
    private AlertDialog.Builder builder;
    private FragmentActivity activity;

public PeticioAdapter(ArrayList<Peticio> peticions, FragmentActivity activity) {
        this.peticions = peticions;
        this.activity = activity;
        builder = new AlertDialog.Builder(activity);
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

    //Assignem valors als components del peticio_item.xml
    /*holder.txtID.setText((int) peticio.getID());
    holder.txtData.setText(peticio.getDate());
    holder.txtEstat.setText(peticio.getState());
    holder.txtDetalls.setText(peticio.getExcipientsList());*/
    }

@Override
public int getItemCount() {
        return peticions.size();
        }

public class PeticioViewHolder extends RecyclerView.ViewHolder {

    private TextView txtID;
    private TextView txtData;
    private TextView txtEstat;
    private TextView txtDetalls;

    public PeticioViewHolder(@NonNull View itemView) {
        super(itemView);
        //Identificadors dels elements del layout de peticio_item
        txtID = itemView.findViewById(R.id.ID);
        txtData = itemView.findViewById(R.id.Data_compra);
        txtEstat = itemView.findViewById(R.id.estat);
        txtDetalls = itemView.findViewById(R.id.detalls);
        //txtDescripcio = itemView.findViewById(R.id.txtDescripcio);
    }

    public void bind(Peticio medicament) {
        //txtNom.setText(medicament.getNom());
        //txtDescripcio.setText(medicament.getDescripcio());
    }
}
}
