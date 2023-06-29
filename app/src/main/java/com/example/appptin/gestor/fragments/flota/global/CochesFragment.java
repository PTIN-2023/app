package com.example.appptin.gestor.fragments.flota.global;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.appptin.R;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class CochesFragment extends Fragment {

    private View view;
    private ImageView iv_regresar;
    private RecyclerView recyclerView_coches;
    private Spinner spinnerSort_coches;
    private String opcionSeleccionada = "";
    boolean ordenAscendente;

    private ArrayList<InformacionCoche> arrayList;
    private ArrayList<InformacionCoche> searchList;

    public CochesFragment() {
        // Required empty public constructor

        //Quitar cuando se implemente método para obtener los datos de la api
        arrayList = new ArrayList<>();
        //arrayList.add(new InformacionCoche("Coche 1",1));
        //arrayList.add(new InformacionCoche("Coche 2",2));
        //arrayList.add(new InformacionCoche("Coche 3",1));

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_coches, container, false);

        //Por defecto ordenar por nombre Ascendiente
        opcionSeleccionada = getResources().getStringArray(R.array.sort_options_flotas)[0];
        recyclerView_coches = view.findViewById(R.id.recyclerView_coches);
        api_cars_full_info();
        Lista(view);

        return view;
    }

    public void Lista(View view) {

        spinnerSort_coches = view.findViewById(R.id.sp_coches);
        iv_regresar = view.findViewById(R.id.iv_coche_back);

        // Quitar cuando se implemente la llamada a la API - Ubicar método justo despues de obtener los datos de la api
        //Creacion_elementos_RecyclerView(arrayList);

        //Adapter para el SPINER
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.sort_options_flotas, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSort_coches.setAdapter(adapter);

        //LISTENERS

        //Listener del Spiner
        spinnerSort_coches.setOnItemSelectedListener(seleccion_spiner);

        iv_regresar.setOnClickListener(regresar);
    }

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

    private AdapterView.OnItemSelectedListener seleccion_spiner = new AdapterView.OnItemSelectedListener(){

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            opcionSeleccionada = adapterView.getItemAtPosition(i).toString();
            // Limpiar texto del SearhView
            ordenarArrayList(arrayList);

        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
            // Acciones a realizar cuando no se selecciona ninguna opción del Spinner
        }
    };

    // Ordenación por Nombre
    Comparator<InformacionCoche> comparadorNombre = new Comparator<InformacionCoche>() {
        @Override
        //Comparar por nombre
        public int compare(InformacionCoche t1, InformacionCoche t2) {
            //Ascendiente
            if(ordenAscendente)
                return t1.geNombreCoche().compareTo(t2.geNombreCoche());
                //Descendiente
            else
                return t2.geNombreCoche().compareTo(t1.geNombreCoche());
        }

    };

    private void ordenarArrayList(ArrayList<InformacionCoche> lista_elementos) {
        //Lee las opciones del fichero values/arrays.xml
        String[] opciones = getResources().getStringArray(R.array.sort_options_flotas);

        if (opcionSeleccionada.equals(opciones[0])) {
            // Ordenar por nombre ascendente
            ordenAscendente = true;
            Collections.sort(arrayList, comparadorNombre);

            //
            Creacion_elementos_RecyclerView(lista_elementos);

        } else if (opcionSeleccionada.equals(opciones[1])) {
            // Ordenar por nombre descendiente
            ordenAscendente = false;
            Collections.sort(arrayList, comparadorNombre);
            Creacion_elementos_RecyclerView(lista_elementos);
        }

    }

    private void Creacion_elementos_RecyclerView(ArrayList<InformacionCoche> lista_elementos) {
        // Creación de LayoutManager que se encarga de la disposición de los elementos del RecyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView_coches.setLayoutManager(layoutManager);

        // Creación del adapter con la nueva lista de elementos búscados
        if (!isAdded()) return;
        CocheAdapter cocheAdapter = new CocheAdapter(getActivity(), lista_elementos, getParentFragmentManager());

        recyclerView_coches.setAdapter(cocheAdapter);
    }

    private void api_cars_full_info(){
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        Resources r = getResources();
        String apiUrl = r.getString(R.string.api_base_url);
        String url = apiUrl + "/api/cars_full_info";
        JSONObject jsonObject = new JSONObject();
        // Parametro a enviar a la api
        try {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserPref", Context.MODE_PRIVATE);
            String session_token = sharedPreferences.getString("session_token", "Valor nulo");  //SI AIXÒ NO FUNCIONA, FER-HO COM LA LINIA DE BAIX
            jsonObject.put("session_token", session_token);
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
                    JSONArray carsArray = response.getJSONArray("cars");

                    for (int i = 0; i < carsArray.length(); i++) {
                        JSONObject carObject = carsArray.getJSONObject(i);

                        int identificador = carObject.getInt("id_car");
                        String matricula = carObject.getString("license_plate");
                        String estat = carObject.getString("status_text");
                        String bateria = carObject.getString("battery");
                        String ultim_manteniment = carObject.getString("last_maintenance_date");
                        JSONArray paquets = carObject.getJSONArray("packages");

                        JSONObject punt_inici = carObject.getJSONObject("location_in ");
                        double latitude_inici = punt_inici.getDouble("latitude");
                        double longitude_inici = punt_inici.getDouble("longitude");

                        JSONObject punt_desti = carObject.getJSONObject("location_end");
                        double latitude_desti = punt_desti.getDouble("latitude");
                        double longitude_desti = punt_desti.getDouble("longitude");

                        JSONObject locationAct = carObject.getJSONObject("location_act");
                        double latitude = locationAct.getDouble("latitude");
                        double longitude = locationAct.getDouble("longitude");

                        arrayList.add(new InformacionCoche(identificador,matricula,estat,bateria,ultim_manteniment,paquets,latitude_inici,
                                                           longitude_inici,latitude_desti,longitude_desti,latitude,longitude));
                    }
                    Creacion_elementos_RecyclerView(arrayList);
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