package com.example.appptin.medico.fragments.recetas;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.appptin.R;
import com.example.appptin.gestor.fragments.flota.global.CochesFragment;
import com.example.appptin.gestor.fragments.flota.local.EdgeCiudadFragment;
import com.example.appptin.medico.fragments.recetas.crear.RecetaFragment;
import com.example.appptin.medico.fragments.recetas.historial.HistorialRecetaFragment;
import com.example.appptin.medico.fragments.recetas.historial.InformacionPreinscripciones;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RecetaConsultarCrearFragment extends Fragment {

    private Button btn_crear, btn_historial;
    private ArrayList<InformacionPreinscripciones> arrayList;
    public RecetaConsultarCrearFragment() {
        // Required empty public constructor

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_receta_consultar_crear, container, false);

        btn_crear = v.findViewById(R.id.button_crear_receta);
        btn_historial = v.findViewById(R.id.button_consultar_receta);

        arrayList = new ArrayList<>();

        //Listener
        btn_crear.setOnClickListener(listener_crear);
        btn_historial.setOnClickListener(listener_historial);

        return v;
    }

    // EVENTOS
    private View.OnClickListener listener_crear = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            RecetaFragment recetaFragment = new RecetaFragment();
            transaction.replace(R.id.frame_container, recetaFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    };

    private View.OnClickListener listener_historial = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mostrar_dialogo();

        }
    };

    private void mostrar_dialogo(){
        // Crear un diálogo personalizado
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Inflar el diseño del diálogo
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.contenedor_solicitar_email, null);
        builder.setView(dialogView);

        // Obtener la referencia al EditText del correo electrónico
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) EditText editTextEmail = dialogView.findViewById(R.id.editTextEmail);

        // Configurar los botones del diálogo
        builder.setPositiveButton("Consultar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Obtener el valor ingresado en el EditText
                String email = editTextEmail.getText().toString();

                api_get_patient_prescription_history(email);


                // Cerrar el diálogo
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Cerrar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Realizar la acción correspondiente al botón "Cerrar"
                // Aquí puedes realizar la lógica para cerrar el diálogo sin hacer nada

                // Cerrar el diálogo
                dialog.dismiss();
            }
        });

        // Mostrar el diálogo
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void llamar_historial_receta(ArrayList<InformacionPreinscripciones> info_recetas){
        
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        HistorialRecetaFragment historialRecetaFragment = new HistorialRecetaFragment(info_recetas);
        transaction.replace(R.id.frame_container, historialRecetaFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void api_get_patient_prescription_history(String email){
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        Resources r = getResources();
        String apiUrl = r.getString(R.string.api_base_url);
        String url = apiUrl + "/api/get_patient_prescription_history";
        JSONObject jsonObject = new JSONObject();
        // Parametro a enviar a la api
        try {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserPref", Context.MODE_PRIVATE);
            String session_token = sharedPreferences.getString("session_token", "Valor nulo");  //SI AIXÒ NO FUNCIONA, FER-HO COM LA LINIA DE BAIX
            jsonObject.put("session_token", session_token);
            jsonObject.put("patient_email", email);
            System.out.println(session_token);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Enviar datos a la api

        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println("MENSAJE: " + response);

                //JSONObject jsonObject = new JSONObject(jsonString);
                JSONArray prescriptionsArray = null;
                try {
                    String result = response.getString("result");
                    if (result.equals("ok")) {
                        prescriptionsArray = response.getJSONArray("prescriptions");
                        if(prescriptionsArray.length()>0) {
                            for (int i = 0; i < prescriptionsArray.length(); i++) {
                                JSONObject prescriptionObject = prescriptionsArray.getJSONObject(i);

                                InformacionPreinscripciones prescription_datos = new InformacionPreinscripciones();
                                prescription_datos.setDuration(prescriptionObject.getString("duration"));
                                prescription_datos.setLastUsed(prescriptionObject.getString("last_used"));
                                prescription_datos.setNotes(prescriptionObject.getString("notes"));
                                prescription_datos.setPrescriptionIdentifier(prescriptionObject.getString("prescription_identifier"));
                                prescription_datos.setRenewal(prescriptionObject.getString("renewal"));
                                prescription_datos.setMedicineList(prescriptionObject.getJSONArray("medicine_list"));
                                prescription_datos.setCont(i + 1);

                                arrayList.add(prescription_datos);
                            }
                            llamar_historial_receta(arrayList);
                        }
                        // Eliminar si los de la api quitan el mail por defecto
                        else{
                            Toast.makeText(getContext(), "Pacient sense receptes", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(getContext(), "No existeix el pacient", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }


            }}, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Manejo de errores de la solicitud
                error.printStackTrace();
            }
        });
        queue.add(jsonArrayRequest);
    }
}