package com.example.appptin.medico.fragments.recetaPaciente;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.appptin.Medicament;
import com.example.appptin.R;
import com.example.appptin.medico.fragments.historialPeticion.InformacionPeticion;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class RecetaFragment extends Fragment {

    EditText nom_pacient, nom_medicament, duracio;
    TextInputEditText notes;
    Button btn_guardar, btn_afegir;
    private PopupWindow popupWindow;

    private static JSONArray list_meds_recipe;
    String p_identifier;
    public RecetaFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recepta, container, false);

        // Asociar elementos del layout
        nom_pacient = view.findViewById(R.id.et_receta_paciente);
        //nom_medicament = view.findViewById(R.id.et_receta_medicamento);
        duracio = view.findViewById(R.id.et_receta_duracion);
        notes = view.findViewById(R.id.txt_receta_notas);
        btn_guardar = view.findViewById(R.id.btn_recepta_guardar);
        btn_afegir = view.findViewById(R.id.btn_afegir_nou_med_recepta);

        //Por defecto botón "Guardar desactivado"
        btn_guardar.setEnabled(false);

        //Listeners
        btn_guardar.setOnClickListener(guardar);
        btn_afegir.setOnClickListener(afegir);
        setNom_PacientListener();

        //setNom_MedicamentListener();
        setDuracioListener();
        setNotesListener();

        //nueva lista
        list_meds_recipe = new JSONArray();

        //arranar el recycler view
        //initRecyclerView(view);

        return view;
    }

//    private void initRecyclerView(View view) {
//        RecyclerView recyclerView = view.findViewById(R.id.recycler_meds_recipe);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
//
//        recyclerView.setLayoutManager(layoutManager);
//
//        recyclerView.setAdapter();
//    }

    private View.OnClickListener afegir = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //abrir popup en el que meter la info de un nuevo medicamento
            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View popupView = inflater.inflate(R.layout.fragment_recepta_popup_nou_med, null);

            popupWindow = new PopupWindow(
                    popupView,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    true
            );

            popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

            Button btn_acceptar = popupView.findViewById(R.id.btn_recepta_acceptar_nou_med);
            Button btn_cancelar = popupView.findViewById(R.id.btn_recepta_cancelar_nou_med);

            btn_acceptar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //obtenemos lost elementos del medicamento y los añadimos al objeto para el recycler view
                    EditText editTextCodi = popupView.findViewById(R.id.txt_receta_codi_popup);
                    EditText editTextQuant = popupView.findViewById(R.id.txt_receta_quant_popup);

                    JSONArray med = new JSONArray();
                    med.put(editTextCodi.getText().toString());
                    med.put(Integer.parseInt(editTextQuant.getText().toString()));
                    System.out.println(med);

                    list_meds_recipe.put(med);
                    popupWindow.dismiss();
                    System.out.println(list_meds_recipe);
                }
            });

            btn_cancelar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //se cancela lo de añadir y se cierra el popup
                    popupWindow.dismiss();
                }
            });
        }
    };

    private View.OnClickListener guardar = new View.OnClickListener() {
        @SuppressLint("SuspiciousIndentation")
        @Override
        public void onClick(View view) {
            // Verificar que no esté vacio
            if(nom_pacient.getText().toString().isEmpty()
                    || duracio.getText().toString().isEmpty()
                    || notes.getText().toString().isEmpty()
                    || list_meds_recipe.length() == 0
            )
                aviso("Omplir totes les dades");
                // Enviar los datos a la api
            else
                api_get_prescription_identifier();
                api_doctor_create_prescription(nom_pacient.getText().toString(),list_meds_recipe,duracio.getText().toString(),notes.getText().toString());
        }
    };

    private void setNom_PacientListener(){
        nom_pacient.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                btn_guardar.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
/*
    private void setNom_MedicamentListener(){
        nom_medicament.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                btn_guardar.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }*/

    private void setDuracioListener(){
        duracio.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                btn_guardar.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void setNotesListener(){
        notes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                btn_guardar.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void api_get_prescription_identifier() {

        System.out.println("get the prescription identifier");

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        Resources r = getResources();
        String apiUrl = r.getString(R.string.api_base_url);
        String url = apiUrl + "/api/get_prescription_identifier";
        JSONObject jsonBody = new JSONObject();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserPref", Context.MODE_PRIVATE);

        try {
            jsonBody.put("session_token", sharedPreferences.getString("session_token", "No value"));
        } catch (JSONException e) {
            e.printStackTrace();
        }



        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println("MENSAJE: " + response);
                try {
                    String result = response.getString("result");

                    // Caso exitoso
                    if (result.equals("ok")) {
                        System.out.println("llego el identifier");
                        System.out.println(response.getString("prescription_identifier"));
                        p_identifier = response.getString("prescription_identifier");
                    }
                    // Caso no exitoso, crear algún dialogo
                    else{
                        System.out.println("error");
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Manejo de errores de la solicitud
                error.printStackTrace();
                Toast.makeText(getActivity(),"Error en el servidor",Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(jsonObjectRequest);

        //api_doctor_create_prescription(nom_pacient.getText().toString(),list_meds_recipe,duracio.getText().toString(),notes.getText().toString());
    }

    private void api_doctor_create_prescription(String paciente, JSONArray list_meds_recipe_full, String duracion, String notas){
        System.out.println("send the new prescription");

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        Resources r = getResources();
        String apiUrl = r.getString(R.string.api_base_url);
        String url = apiUrl + "/api/doctor_create_prescription";
        JSONObject jsonBody = new JSONObject();

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserPref", Context.MODE_PRIVATE);

        System.out.println("pidentifiet: " + p_identifier);

        // Datos enviados
        try {
            //jsonBody.put("session_token", login.getSession_token());
            jsonBody.put("session_token", sharedPreferences.getString("session_token", "No value"));
            jsonBody.put("user_full_name", paciente);
            jsonBody.put("prescription_identifier",p_identifier);
            //Obtener la lista de códigos:
            jsonBody.put("medicine_list", list_meds_recipe_full);

            jsonBody.put("duration", duracion);
            jsonBody.put("notes", notas);
            System.out.println("Mensaje a enviar: " + jsonBody);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Datos devueltos
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println("MENSAJE: " + response);
                try {
                    String result = response.getString("result");

                    // Caso exitoso
                    if (result.equals("ok")) {
                        Toast.makeText(getActivity(),"Recepta creada",Toast.LENGTH_SHORT).show();
                    }

                    // Caso no exitoso, crear algún dialogo
                    else{
                        Toast.makeText(getActivity(),"Error recepta",Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Manejo de errores de la solicitud
                error.printStackTrace();
                Toast.makeText(getActivity(),"Error en el servidor",Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(jsonObjectRequest);
    }

    private void aviso(String mensaje){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(mensaje)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Acciones a realizar al hacer clic en el botón "Aceptar"
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();

        // Obtener el botón "Aceptar" del diálogo
        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        // Establecer la gravedad del botón en el centro
        positiveButton.setGravity(Gravity.CENTER);
    }
}