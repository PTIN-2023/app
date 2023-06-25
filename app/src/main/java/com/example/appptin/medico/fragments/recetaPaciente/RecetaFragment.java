package com.example.appptin.medico.fragments.recetaPaciente;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
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
    Button btn_guardar;
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
        nom_medicament = view.findViewById(R.id.et_receta_medicamento);
        duracio = view.findViewById(R.id.et_receta_duracion);
        notes = view.findViewById(R.id.txt_receta_notas);
        btn_guardar = view.findViewById(R.id.btn_recepta_guardar);

        //Por defecto botón "Guardar desactivado"
        btn_guardar.setEnabled(false);

        //Listeners
        btn_guardar.setOnClickListener(guardar);
        setNom_PacientListener();
        setNom_MedicamentListener();
        setDuracioListener();
        setNotesListener();

        return view;
    }

    private View.OnClickListener guardar = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            // Verificar que no esté vacio
            if(nom_pacient.getText().toString().isEmpty()
                    || nom_medicament.getText().toString().isEmpty()
                    || duracio.getText().toString().isEmpty()
                    || notes.getText().toString().isEmpty()
              )
                aviso("Omplir totes les dades");
            // Enviar los datos a la api
            else
                api_doctor_create_prescription(nom_pacient.getText().toString(),nom_medicament.getText().toString(),duracio.getText().toString(),notes.getText().toString());
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
    }

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

    private void api_doctor_create_prescription(String paciente, String medicamento, String duracion, String notas){
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        Resources r = getResources();
        String apiUrl = r.getString(R.string.api_base_url);
        String url = apiUrl + "/api/doctor_create_prescription";
        JSONObject jsonBody = new JSONObject();

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserPref", Context.MODE_PRIVATE);

        // Datos enviados
        try {
            //jsonBody.put("session_token", login.getSession_token());
            jsonBody.put("session_token", sharedPreferences.getString("session_token", "No value"));
            jsonBody.put("user_full_name", paciente);

            //Obtener la lista de códigos:
            String[] codigos = medicamento.split(",");
            JSONArray jsonArray = new JSONArray(codigos);
            jsonBody.put("medicine_list", jsonArray);

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