package com.example.appptin.paciente.opciones;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.appptin.Peticio;
import com.example.appptin.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PeticioAdapter extends RecyclerView.Adapter<PeticioAdapter.PeticioViewHolder> {


    private ArrayList<Peticio> peticions;
    private AlertDialog.Builder builder;
    private FragmentActivity activity;

public PeticioAdapter(ArrayList<Peticio> peticions, FragmentActivity activity) {
        this.peticions = peticions;
        this.activity = activity;
        if (activity != null) {
        builder = new AlertDialog.Builder(activity);
        }
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
    //holder.bind(peticio);
    String estat = null;
    SpannableString spannableString = null;
    ForegroundColorSpan colorSpan = null;

    //Assignem valors als components del peticio_item.xml
    //holder.txtID.setText(String.valueOf(peticio.getID()));
    holder.txtData.setText(peticio.getDate());
    holder.txtEstat.setText(peticio.getState());
    //holder.txtDetalls.setText(peticio.getExcipientsList());


    if (peticio.getState().equals("awaiting_confirmation")) {
        estat = "Esperant confirmació";
        spannableString = SpannableString.valueOf(estat);
        colorSpan = new ForegroundColorSpan(Color.BLUE);
        spannableString.setSpan(colorSpan, 0, estat.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.txtEstat.setText(spannableString);
    } else if (peticio.getState().equals("ordered")) {
        estat = "Ordenat";
        spannableString = SpannableString.valueOf(estat);
        colorSpan = new ForegroundColorSpan(Color.BLUE);
        spannableString.setSpan(colorSpan, 0, estat.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.txtEstat.setText(spannableString);
    } else if (peticio.getState().equals("car_sent")) {
        estat = "Cotxe enviat";
        spannableString = SpannableString.valueOf(estat);
        colorSpan = new ForegroundColorSpan(Color.MAGENTA);
        spannableString.setSpan(colorSpan, 0, estat.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.txtEstat.setText(spannableString);
    } else if (peticio.getState().equals("drone_sent")) {
        estat = "Dron enviat";
        spannableString = SpannableString.valueOf(estat);
        colorSpan = new ForegroundColorSpan(Color.MAGENTA);
        spannableString.setSpan(colorSpan, 0, estat.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.txtEstat.setText(spannableString);
    } else if (peticio.getState().equals("delivered_awaiting")) {
        estat = "En repartiment";
        spannableString = SpannableString.valueOf(estat);
        colorSpan = new ForegroundColorSpan(Color.BLUE);
        spannableString.setSpan(colorSpan, 0, estat.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.txtEstat.setText(spannableString);
    } else if (peticio.getState().equals("delivered")) {
        estat = "Entregat";
        spannableString = SpannableString.valueOf(estat);
        colorSpan = new ForegroundColorSpan(Color.GREEN);
        spannableString.setSpan(colorSpan, 0, estat.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.txtEstat.setText(spannableString);
    } else if (peticio.getState().equals("denied")) {
        estat = "Denegat";
        spannableString = SpannableString.valueOf(estat);
        colorSpan = new ForegroundColorSpan(Color.RED);
        spannableString.setSpan(colorSpan, 0, estat.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.txtEstat.setText(spannableString);
    } else if (peticio.getState().equals("canceled")) {
        estat = "Cancel·lat";
        spannableString = SpannableString.valueOf(estat);
        colorSpan = new ForegroundColorSpan(Color.RED);
        spannableString.setSpan(colorSpan, 0, estat.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.txtEstat.setText(spannableString);
    }

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
                } else {
                    stringBuilder.append("Prescripció requerida: ").append("No").append("\n");
                }
                stringBuilder.append("Preu: ").append(pvp).append("" + "€").append("\n\n");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        builder.setMessage(stringBuilder.toString());

        String state = peticio.getState();
        if (state.equals("awaiting_confirmation") || state.equals("ordered")) {
            builder.setNegativeButton("Cancel·lar petició", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    AlertDialog.Builder cancelDialog = new AlertDialog.Builder(activity);
                    cancelDialog.setTitle("Confirmar cancel·lació");

                    // Crear una SpannableString per assignar la mida del text
                    SpannableString spannableString = new SpannableString("Segur que vol cancel·lar la comanda?");
                    //Això augmenta mida lletra del pop-up
                    spannableString.setSpan(new RelativeSizeSpan(1.2f), 0, spannableString.length(), 0);

                    cancelDialog.setMessage(spannableString);
                    cancelDialog.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Lògica per a cancel·lar la comanda
                            RequestQueue queue = Volley.newRequestQueue(activity);
                            Resources r = activity.getResources();
                            cancelarComanda(peticio);
                            //String nou_state = "canceled";
                            //peticio.setState(nou_state);
                        }
                    });
                    cancelDialog.setNegativeButton("No", null);
                    AlertDialog cancelAlertDialog = cancelDialog.create();
                    cancelAlertDialog.show();

                    // Posicionar el botó de cancel·lar a la part d'abaix, en el mig
                    Button cancelButton = cancelAlertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                    LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) cancelButton.getLayoutParams();
                    layoutParams.gravity = Gravity.CENTER;
                    layoutParams.weight = 1;
                    cancelButton.setLayoutParams(layoutParams);
                }
            });
        }

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        // Afegir el botó de tancar (x) a la part superior dreta del diàleg
            int closeButtonId = Resources.getSystem().getIdentifier("close", "id", "android");
            View closeButton = alertDialog.findViewById(closeButtonId);
            if (closeButton != null) {
                closeButton.setVisibility(View.GONE);
            }

            int alertTitleId = Resources.getSystem().getIdentifier("alertTitle", "id", "android");
            TextView alertTitle = alertDialog.findViewById(alertTitleId);
            if (alertTitle != null) {
                alertTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_close, 0);
                alertTitle.setCompoundDrawablePadding(20);
                alertTitle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
            }

    }


    private void cancelarComanda(Peticio peticio) {
        RequestQueue queue = Volley.newRequestQueue(activity);
        Resources r = activity.getResources();
        String apiUrl = r.getString(R.string.api_base_url);
        String url = apiUrl + "/api/cancel_patient_order";
        JSONObject jsonObject = new JSONObject();

        try {
            SharedPreferences sharedPreferences = activity.getSharedPreferences("UserPref", Context.MODE_PRIVATE);
            String session_token = sharedPreferences.getString("session_token", "Valor nulo");
            //et_email= sharedPreferences.getString("user_email", "Valor nulo");;
            System.out.println(session_token);

            jsonObject.put("session_token", session_token);
            String id = String.valueOf(peticio.getID());
            jsonObject.put("order_identifier", id);
            System.out.println(jsonObject);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("MENSAJE: " + response);
                        notifyDataSetChanged();
                    }
                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // Aquí pots tractar els errors de la crida a l'API
                                // per exemple, mostrar un missatge d'error
                                Toast.makeText(activity, "Error en cancel·lar la comanda", Toast.LENGTH_SHORT).show();
                            }
                        }
                );
        // Afegir la petició a la cua de peticions
        queue.add(jsonObjectRequest);
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
