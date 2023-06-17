package com.example.appptin.gestor.fragments.pefilgestor.opciones;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appptin.Peticio;
import com.example.appptin.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PeticioGestorAdapter extends RecyclerView.Adapter<PeticioGestorAdapter.PeticioViewHolder> {

    private ArrayList<Peticio> peticions;
    private AlertDialog.Builder builder;
    private FragmentActivity activity;

    public PeticioGestorAdapter(ArrayList<Peticio> peticions, FragmentActivity activity) {
        this.peticions = peticions;
        this.activity = activity;
        if (activity != null) {
            builder = new AlertDialog.Builder(activity);
        }
    }

    @NonNull
    @Override
    public PeticioGestorAdapter.PeticioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.peticio_item, parent, false);
        return new PeticioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PeticioGestorAdapter.PeticioViewHolder holder, int position) {
        Peticio peticio = peticions.get(position);
        holder.bind(peticio);

        //Assignem valors als components del peticio_item.xml
        holder.txtID.setText(String.valueOf(peticio.getID()));
        holder.txtData.setText(peticio.getDate());
        holder.txtEstat.setText(peticio.getState());
        //holder.txtDetalls.setText(peticio.getExcipientsList());

        // Crea el diàleg d'alerta en el clic del botó "btn_detalls"
        holder.BtnDetalls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDetallsMedicaments(peticio);
            }
        });
    }

    private void mostrarDetallsMedicaments(Peticio peticio) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Detalls dels medicaments");

        ArrayList<JSONObject> medicines = peticio.getMedicines();
        StringBuilder stringBuilder = new StringBuilder();
        for (JSONObject medicine : medicines) {
            try {
                String medName = medicine.getString("med_name");
                String form = medicine.getString("form");
                String typeOfAdministration = medicine.getString("type_of_administration");
                boolean prescriptionNeeded = medicine.getBoolean("prescription_needed");
                double pvp = medicine.getDouble("pvp");

                stringBuilder.append("Nom del medicament: ").append(medName).append("\n");
                stringBuilder.append("Forma: ").append(form).append("\n");
                stringBuilder.append("Administració: ").append(typeOfAdministration).append("\n");
                if (prescriptionNeeded == true) {
                    stringBuilder.append("Prescripció requerida: ").append("Sí").append("\n");
                }
                else {
                    stringBuilder.append("Prescripció requerida: ").append("No").append("\n");
                }
                stringBuilder.append("Preu: ").append(pvp).append("" + "€").append("\n\n");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        builder.setMessage(stringBuilder.toString());
        builder.setPositiveButton("Acceptar", null);
        builder.show();
    }

    @Override
    public int getItemCount() {
        return peticions.size();
    }

    public class PeticioViewHolder extends RecyclerView.ViewHolder {

        private TextView txtID;
        private TextView txtData;
        private TextView txtEstat;
        private ImageButton BtnDetalls;

        public PeticioViewHolder(@NonNull View itemView) {
            super(itemView);
            //Identificadors dels elements del layout de peticio_item
            //txtID = itemView.findViewById(R.id.ID);
            txtData = itemView.findViewById(R.id.Data_compra);
            txtEstat = itemView.findViewById(R.id.estat);
            BtnDetalls = itemView.findViewById(R.id.btn_detalls);
        }

        public void bind(Peticio medicament) {
            //txtNom.setText(medicament.getNom());
            //txtDescripcio.setText(medicament.getDescripcio());
        }
    }
}

