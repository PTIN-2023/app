package com.example.appptin.paciente.opciones;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.appptin.R;
import com.example.appptin.medico.fragments.recetas.historial.HistorialRecetaAdapter;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RecetasPacienteFragment extends Fragment {

    private View view;
    private ImageView iv_regresar;
    private RecyclerView recyclerView_receta;
    ArrayList<InformacionRecetaPAciente> arrayList;
    public RecetasPacienteFragment() {
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

        view = inflater.inflate(R.layout.fragment_recetas_paciente, container, false);
        recyclerView_receta = view.findViewById(R.id.recyclerView_historial_receta_paciente);
        arrayList = new ArrayList<>();
        api_get_patient_prescription_history();
        Lista(view);

        return view;
    }

    public void Lista(View view) {

        iv_regresar = view.findViewById(R.id.iv_historial_receta_paciente_back);

        // Quitar cuando se implemente la llamada a la API - Ubicar método justo despues de obtener los datos de la api
        Creacion_elementos_RecyclerView(arrayList);

        //LISTENERS
        iv_regresar.setOnClickListener(regresar);
    }

    private View.OnClickListener regresar = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            anterior_ventana();
        }
    };

    private void Creacion_elementos_RecyclerView(ArrayList<InformacionRecetaPAciente> lista_elementos) {
        // Creación de LayoutManager que se encarga de la disposición de los elementos del RecyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView_receta.setLayoutManager(layoutManager);

        // Creación del adapter con la nueva lista de elementos búscados
        if (!isAdded()) return;
        RecetasPacienteAdapter recetasPacienteAdapter = new RecetasPacienteAdapter(getActivity(), lista_elementos, getParentFragmentManager());

        recyclerView_receta.setAdapter(recetasPacienteAdapter);
    }

    private void anterior_ventana(){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager(); // Si estás en un Fragment, utiliza getFragmentManager()
        if (fragmentManager.getBackStackEntryCount() > 0) {
            // Retrocede en la pila de fragmentos
            fragmentManager.popBackStack();
        }
    }

    private void api_get_patient_prescription_history(){
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        Resources r = getResources();
        String apiUrl = r.getString(R.string.api_base_url);
        String url = apiUrl + "/api/get_patient_prescription_history";
        JSONObject jsonObject = new JSONObject();
        // Parametro a enviar a la api
        try {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserPref", Context.MODE_PRIVATE);
            String email = sharedPreferences.getString("user_email", "Valor vacio");
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
                        if(prescriptionsArray.length()>0){
                            for (int i = 0; i < prescriptionsArray.length(); i++) {
                                JSONObject prescriptionObject = prescriptionsArray.getJSONObject(i);

                                InformacionRecetaPAciente prescription_datos = new InformacionRecetaPAciente();
                                prescription_datos.setDuration(prescriptionObject.getString("duration"));
                                prescription_datos.setLastUsed(prescriptionObject.getString("last_used"));
                                prescription_datos.setNotes(prescriptionObject.getString("notes"));
                                prescription_datos.setPrescriptionIdentifier(prescriptionObject.getString("prescription_identifier"));
                                prescription_datos.setRenewal(prescriptionObject.getString("renewal"));
                                prescription_datos.setMedicineList(prescriptionObject.getJSONArray("medicine_list"));
                                prescription_datos.setCont(i+1);

                                arrayList.add(prescription_datos);
                            }
                            Creacion_elementos_RecyclerView(arrayList);
                        }
                        // Eliminar si los de la api quitan el mail por defecto
                        else{
                            Toast.makeText(getContext(), "Pacient sense receptes", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(getContext(), "Pacient sense receptes", Toast.LENGTH_SHORT).show();
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