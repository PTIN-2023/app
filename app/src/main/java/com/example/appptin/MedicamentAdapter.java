package com.example.appptin;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MedicamentAdapter extends RecyclerView.Adapter<MedicamentAdapter.MedicamentViewHolder> {

    private final FragmentActivity context;
    private ArrayList<Medicament> medicaments;
    private AlertDialog.Builder builder;
    private FragmentActivity activity;

    public MedicamentAdapter(ArrayList<Medicament> medicaments, FragmentActivity context) {
        this.medicaments = medicaments;
        this.context = context;
        if (context != null) {
            builder = new AlertDialog.Builder(context);
        }
    }

    @NonNull
    @Override
    public MedicamentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.medicament_item, parent, false);
        return new MedicamentViewHolder(view);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull MedicamentViewHolder holder, int position) {
        Medicament medicament = medicaments.get(position);
        //holder.bind(medicament);

        // Asignar el valor de los componentes
        holder.txtNom.setText(medicament.getMedName());
        holder.txtPvp.setText(String.valueOf(medicament.getPvp()) + "€");

        //Aquí asignariamos las imagenes con la URL que devuelve la API,
        Glide.with(holder.imgMedicament.getContext())
                .load(medicaments.get(position).getURLimage())
                .into(holder.imgMedicament);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Medicament medicament = medicaments.get(position);
                //CrearDialogoMedicamento(medicament);
                holder.CrearDialogoMedicamento(medicament);

            }
        });
    }


    @Override
    public int getItemCount() {
        return medicaments.size();
    }

    public class MedicamentViewHolder extends RecyclerView.ViewHolder {

        private TextView txtNom;
        private TextView txtPvp;
        private ImageView imgMedicament;

        public MedicamentViewHolder(@NonNull View itemView) {
            super(itemView);
            //Identificadors dels elements del layout de medicament_item
            txtNom = itemView.findViewById(R.id.med_Nom);
            txtPvp = itemView.findViewById(R.id.med_Pvp);
            imgMedicament = itemView.findViewById(R.id.medicament_image);
            //itemView.setOnClickListener(this);
        }


        private void CrearDialogoMedicamento(Medicament medicament) {

            // Crear un objeto AlertDialog.Builder
            //builder = new AlertDialog.Builder(activity);

            // Crear un objeto SpannableStringBuilder para formatear el texto
            SpannableStringBuilder messageBuilder = new SpannableStringBuilder();

            // Agregar el texto normal y aplicar formato negrita a los valores de los campos
            messageBuilder.append("\n");
            if (medicament.getMedName() != null) {
                messageBuilder.append(formatInBold(" - Nom: ")).append(medicament.getMedName()).append("\n");
            }
            if (medicament.getNationalCode() != null) {
                messageBuilder.append(formatInBold(" - Codi Nacional: ")).append(medicament.getNationalCode()).append("\n");
            }
            if (medicament.getUseType() != null) {
                messageBuilder.append(formatInBold(" - Tipus: ")).append(medicament.getUseType()).append("\n");
            }
            if (medicament.getTypeOfAdministration() != null) {
                messageBuilder.append(formatInBold(" - Administració: ")).append(medicament.getTypeOfAdministration()).append("\n");
            }
            if (medicament.getPrescriptionNeeded() != null) {
                messageBuilder.append(formatInBold(" - Prescripció: ")).append(medicament.getPrescriptionNeeded()).append("\n");
            }
            if (medicament.getPvp() != -1) {
                messageBuilder.append(formatInBold(" - Preu: ")).append(String.valueOf(medicament.getPvp())).append("€ \n");
            }
            if (medicament.getForm() != null) {
                messageBuilder.append(formatInBold(" - Forma: ")).append(medicament.getForm()).append("\n");
            }
            if (medicament.getExcipientsList() != null) {
                messageBuilder.append(formatInBold(" - Excipients: \n")).append(medicament.getExcipientsList()).append(" \n");
            }// Establecer el título y el mensaje del diálogo
            builder.setTitle("Detalls del medicament");
            builder.setMessage(messageBuilder);

            // Establecer el botón "Añadir a la cesta"
            builder.setPositiveButton("Afegir", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Acción a realizar al hacer clic en "Añadir a la cesta"
                    // Por ejemplo, agregar el producto a la cesta
                    try {
                        addToCart(medicament);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            });

            // Establecer el botón "Cancelar"
            builder.setNegativeButton("cancel·lar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // cerrar el diálogo
                    dialog.dismiss();
                }
            });

            // Crear y mostrar el diálogo
            builder.create();
            builder.show();


        }

        private void addToCart(Medicament medicament) throws JSONException {
            //Comprobar si el elemento existe
            int indice = MainActivity.existeMedicamento(medicament.getNationalCode());
            if (indice < 0) {
                JSONObject objeto = new JSONObject();
                objeto.put("nationalCode", medicament.getNationalCode());
                objeto.put("medName", medicament.getMedName());
                objeto.put("pvp", medicament.getPvp());
                objeto.put("quantitat", 1); //por defecto
                objeto.put("quantity_available", medicament.getLimitQuantity());

                // Guardar el objeto en el JSONArray publico
                MainActivity.setListaMedicamentos(objeto);

                Toast.makeText(itemView.getContext(), "Afegit " + medicament.getMedName(), Toast.LENGTH_SHORT).show();
            } else {
                if(medicament.getLimitQuantity()>MainActivity.getCantidadMedicamento(indice))
                    MainActivity.setCantidadMedicamento(indice, 1);
                else
                    Toast.makeText(itemView.getContext(), "No és possible afegir més", Toast.LENGTH_SHORT).show();
            }
        }

        private SpannableString formatInBold(String text) {
            SpannableString spannableString = new SpannableString(text);
            spannableString.setSpan(new StyleSpan(Typeface.BOLD), 0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            return spannableString;
        }
    }
}

