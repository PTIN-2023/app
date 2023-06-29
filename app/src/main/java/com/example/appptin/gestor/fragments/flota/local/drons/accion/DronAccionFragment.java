package com.example.appptin.gestor.fragments.flota.local.drons.accion;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.appptin.R;

import com.example.appptin.gestor.fragments.flota.local.drons.InformacionDron;

import org.json.JSONException;
import org.json.JSONObject;


public class DronAccionFragment extends Fragment {

    private Button btn_info,btn_confirmar;
    private TextView txt_titulo;
    private ImageView iv_regresar;
    private View view;
    private InformacionDron peticion;
    private Spinner spinneropciones;

    private String opcionSeleccionada = "";
    public DronAccionFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_dron_accion, container, false);

        //Por defecto primera opcion
        opcionSeleccionada = getResources().getStringArray(R.array.sort_options)[0];

        iv_regresar = view.findViewById(R.id.iv_dron_accion_back);
        txt_titulo = view.findViewById(R.id.txt_dron_accion_titulo);
        btn_info = view.findViewById(R.id.btn_dron_accion_info);

        btn_confirmar =  view.findViewById(R.id.btn_validate_dron);
        spinneropciones = view.findViewById(R.id.spinner_options_dron);

        //Adapter para el SPINER
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.opciones_anomalias_dron, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinneropciones.setAdapter(adapter);

        //Listener
        iv_regresar.setOnClickListener(regresar);
        btn_info.setOnClickListener(listener_info);
        btn_confirmar.setOnClickListener(listener_confirmar);
        spinneropciones.setOnItemSelectedListener(seleccion_spiner);

        return view;
    }

    private View.OnClickListener listener_confirmar = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //Toast.makeText(getContext(),opcionSeleccionada,Toast.LENGTH_SHORT).show();
            send_drone_hehe(opcionSeleccionada);
        }
    };

    private AdapterView.OnItemSelectedListener seleccion_spiner = new AdapterView.OnItemSelectedListener(){

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            //Optener opcion
            opcionSeleccionada = adapterView.getItemAtPosition(i).toString();
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
            // Acciones a realizar cuando no se selecciona ninguna opción del Spinner
        }
    };
    private View.OnClickListener regresar = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            FragmentManager fragmentManager = getActivity().getSupportFragmentManager(); // Si estás en un Fragment, utiliza getFragmentManager()
            if (fragmentManager.getBackStackEntryCount() > 0) {
                // Retrocede en la pila de fragmentos
                fragmentManager.popBackStack();
            }
        }
    };


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Obtener resultados recibidos del Fragment principal (Historial peticion)
        getParentFragmentManager().setFragmentResultListener("key_info_dron", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {

                peticion = (InformacionDron) result.getSerializable("Info_dron");
                txt_titulo.setText(txt_titulo.getText().toString()+" "+peticion.getNombre_dron());

            }
        });
    }


    // EVENTOS
    private View.OnClickListener listener_info = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            try {
                Dialogo_info();
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

        }
    };

    private void Dialogo_info() throws JSONException {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Información "+peticion.getNombre_dron());

        StringBuilder message = new StringBuilder();
        message.append("Identificador: ").append(peticion.getIdentificador()).append("\n");
        message.append("Id Colmena: ").append(peticion.getId_beehive()).append("\n");
        message.append("Estado: ").append(peticion.getEstat()).append("\n");
        message.append("Batería: ").append(peticion.getBateria()).append("\n");
        message.append("Último mantenimiento: ").append(peticion.getUltim_manteniment()).append("\n");


        message.append("Posición actual:").append("\n");
        message.append("    Latitud: ").append(String.valueOf(peticion.getLocationAct().getDouble("latitude"))).append("\n");
        message.append("    Longitud: ").append(String.valueOf(peticion.getLocationAct().getDouble("longitude"))).append("\n");

        builder.setMessage(message.toString());
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Acciones al hacer clic en el botón Aceptar
            }
        });

        builder.show();
    }

    private void send_drone_hehe(String anomalia){
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        Resources r = getResources();
        String apiUrl = r.getString(R.string.api_base_url);
        String url = apiUrl + "/api/send_drone_hehe";
        JSONObject jsonObject = new JSONObject();
        // Parametro a enviar a la api
        try {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserPref", Context.MODE_PRIVATE);
            String session_token = sharedPreferences.getString("session_token", "Valor nulo");  //SI AIXÒ NO FUNCIONA, FER-HO COM LA LINIA DE BAIX
            jsonObject.put("session_token", session_token);
            jsonObject.put("id_dron", peticion.getIdentificador());
            jsonObject.put("hehe", anomalia);
            System.out.println(session_token);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Enviar datos a la api
        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println("MENSAJE: " + response);
                try {
                    if (response.get("result").equals("ok")) {
                        Toast.makeText(getContext(),"Realitzat "+opcionSeleccionada,Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(getContext(),"Error dades rebudes",Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }}, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Manejo de errores de la solicitud
                Toast.makeText(getContext(),"Error servidor",Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        });
        queue.add(jsonArrayRequest);
    }
}