package com.example.appptin;

import static java.security.AccessController.getContext;

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
import androidx.appcompat.view.menu.MenuView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appptin.MainActivity;
import com.example.appptin.Medicament;
import com.example.appptin.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    public class MedicamentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView txtNom;
        private TextView txtPvp;
        private ImageView imgMedicament;

        public MedicamentViewHolder(@NonNull View itemView) {
            super(itemView);
            //Identificadors dels elements del layout de medicament_item
            txtNom = itemView.findViewById(R.id.med_Nom);
            txtPvp = itemView.findViewById(R.id.med_Pvp);
            imgMedicament = itemView.findViewById(R.id.medicament_image);
            itemView.setOnClickListener(this);
        }

        public void bind(Medicament medicament) {
            txtNom.setText(medicament.getMedName());
            txtPvp.setText(String.valueOf(medicament.getPvp()) + "€");
            // Lògica per assignar la imatge basada en el nom del medicament
            if (medicament.getMedName().equals("Paracetamol")) {
                imgMedicament.setImageResource(R.drawable.paracetamol);
            } else if (medicament.getMedName().equals("Ibuprofeno")) {
                imgMedicament.setImageResource(R.drawable.ibuprofe);
            } else if (medicament.getMedName().equals("Amoxicilina")) {
                imgMedicament.setImageResource(R.drawable.amoxicilina);
            } else if (medicament.getMedName().equals("Lorazepam")) {
            imgMedicament.setImageResource(R.drawable.lorazepam);
            } else if (medicament.getMedName().equals("Diclofenac")) {
                imgMedicament.setImageResource(R.drawable.diclofenaco);
            } else if (medicament.getMedName().equals("Hydrocortisone")) {
                imgMedicament.setImageResource(R.drawable.hydrocortisona);
            } else if (medicament.getMedName().equals("Talcum Powder")) {
                imgMedicament.setImageResource(R.drawable.talcum_powder);
            } else if (medicament.getMedName().equals("Jarabe per a la Toss")) {
                imgMedicament.setImageResource(R.drawable.flutox);
            }
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Medicament medicament = medicaments.get(position);
                CrearDialogoMedicamento(medicament);
            }
        }

        private void CrearDialogoMedicamento(Medicament medicament) {

            // Crear un objeto AlertDialog.Builder
            AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());

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

            // Mostrar el diálogo
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

                MainActivity.setListaMedicamentos(objeto);
                JSONArray lista = MainActivity.getListaMedicamentos();
                Toast.makeText(itemView.getContext(), "Afegit " + medicament.getMedName(), Toast.LENGTH_SHORT).show();
            } else {
                MainActivity.getCantidadMedicamento(indice, 1);
            }
        }

        private SpannableString formatInBold(String text) {
            SpannableString spannableString = new SpannableString(text);
            spannableString.setSpan(new StyleSpan(Typeface.BOLD), 0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            return spannableString;
        }
    }
}
