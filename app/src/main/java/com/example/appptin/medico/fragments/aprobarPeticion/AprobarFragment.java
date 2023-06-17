package com.example.appptin.medico.fragments.aprobarPeticion;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.appptin.Medicament;
import com.example.appptin.R;
import com.example.appptin.login;
import com.example.appptin.medico.conexion.Conexion_json;
import com.example.appptin.medico.fragments.historialPeticion.HistorialPeticionFragment;
import com.example.appptin.medico.fragments.historialPeticion.InformacionPeticion;
import com.example.appptin.medico.fragments.historialPeticion.PeticionClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;


public class AprobarFragment extends Fragment {
    private static final String TAG = "AprobarFragment";
    RecyclerView recyclerView;
    ArrayList<String> medicamentos = new ArrayList<String>();
    TextView textView_nombre, txt_fecha_peticion, textView_pedido;
    Button btn_aceptar, btn_rechazar;
    int posicion;
    FragmentManager activity;
    InformacionPeticion peticion;
    Context context;

    public AprobarFragment(FragmentManager activity, Context context) {
        // Required empty public constructor
        this.activity = activity;
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_aprobar, container, false);
        Lista_medicamentos(view);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Obtener resultados recibidos del Fragment principal (Historial peticion)
        getParentFragmentManager().setFragmentResultListener("key_aprobar_peticion", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {

                peticion = (InformacionPeticion) result.getSerializable("MiObjeto");

                // Log.d("JSON", peticion.toString());

                textView_nombre.setText(peticion.getPatient_fullname());
                txt_fecha_peticion.setText(peticion.getDate());
                textView_pedido.setText(peticion.getOrder_identifier());
                

                Map<String, Medicament> list_medicament = peticion.getMedicine_list();

                for (Map.Entry<String, Medicament> entry : list_medicament.entrySet()) {
                    String key = entry.getKey();
                    Medicament value = entry.getValue();
                    System.out.println("Clave: " + key + ", Valor: " + value);

                    medicamentos.add(entry.getValue().getMedName());
                }


            }
        });

    }

    public void Lista_medicamentos(View view) {
        //Asociación de los objetos creados en el XML (diseño)
        recyclerView = view.findViewById(R.id.recyclerView_aprobar);

        textView_nombre = view.findViewById(R.id.txt_peticion_nombre);
        txt_fecha_peticion = view.findViewById(R.id.txt_fecha_peticion_aprobar);
        textView_pedido = view.findViewById(R.id.txt_peticion_pedido);

        btn_aceptar = view.findViewById(R.id.btn_aceptar_peticion);
        btn_rechazar = view.findViewById(R.id.btn_rechazar_peticion);


        //Creación de LayoutManager que se encarga de la disposición de los elementos del RecyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        //Asociación de adapter que se encarga de adaptar los datos a lo que finalmente verá el usuario.
        // Es el encargado de traducir datos en UI.
        MedicamentosAdapter medicamentosAdapter = new MedicamentosAdapter(getActivity(), medicamentos);
        recyclerView.setAdapter(medicamentosAdapter);

        // Se acepta la petición del paciente
        btn_aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Petició aceptada", Toast.LENGTH_SHORT).show();

                // Regresar a la ventana anterior
                regresar_ventana();

                // Enviar confirmación de orden a la API
                api_confimacion_denegacion_orden(true);
            }
        });

        btn_rechazar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Petició Rebutjada", Toast.LENGTH_SHORT).show();

                // Regresar a la ventana anterior
                regresar_ventana();

                // Enviar denegación de orden a la API
                api_confimacion_denegacion_orden(false);
            }
        });

    }

    // Regresar a la ventana Madre
    private void regresar_ventana(){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            // Retrocede en la pila de fragmentos
            fragmentManager.popBackStack();
        }
    }

    // API
    private void api_confimacion_denegacion_orden(boolean approved){
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        Resources r = getResources();
        String apiUrl = r.getString(R.string.api_base_url);
        String url = apiUrl + "/api/doctor_confirm_order";
        JSONArray jsonBody = new JSONArray();

        // Datos enviados
        try {
            JSONObject jsonObject = new JSONObject();

            jsonObject.put("session_token", login.getSession_token());
            //jsonObject.put("order_identifier", ??????);
            jsonObject.put("approved", approved);
            //jsonObject.put("reason", "????????????");

            jsonBody.put(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Datos devueltos
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, url, jsonBody, new Response.Listener<JSONArray>() {
            public void onResponse(JSONArray response) {
                // Manejar la respuesta exitosa
                System.out.println(" ******** Se reciben datos *******");
                // Otro código de manejo de la respuesta JSON
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Manejo de errores de la solicitud
                        error.printStackTrace();

                        if (error.networkResponse != null && error.networkResponse.statusCode == 500) {
                            // Error interno del servidor (código de respuesta 500)
                            Log.e(TAG, "FALLO EN EL SERVIDOR : "+ url );
                        } else {
                            // Otro tipo de error de solicitud
                            Log.e(TAG, "FALLO EN EL CLIENTE");
                        }
                    }
                });

        queue.add(jsonArrayRequest);
    }
}
