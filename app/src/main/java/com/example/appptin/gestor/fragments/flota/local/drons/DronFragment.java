package com.example.appptin.gestor.fragments.flota.local.drons;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.content.res.Resources;
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

public class DronFragment extends Fragment {

    private View view;
    private ImageView iv_regresar;
    private RecyclerView recyclerView_dron;
    private Spinner spinnerSort_dron;
    private String opcionSeleccionada = "";
    boolean ordenAscendente;

    int numEdge;
    private ArrayList<InformacionDron> arrayList;
    private ArrayList<InformacionDron> searchList;

    public DronFragment(int numedge) {
        numedge = numedge;
    }
    //public DronFragment(int edge) {
    //    //Quitar cuando se implemente método para obtener los datos de la api
//
    //    arrayList = new ArrayList<>();
    //    arrayList.add(new InformacionDron(identificador, "Dron 1", estat, bateria, ultim_manteniment, id_order, 1, punt_inici, punt_desti, locationAct));
    //    arrayList.add(new InformacionDron(identificador, "Dron 2", estat, bateria, ultim_manteniment, id_order, 1, punt_inici, punt_desti, locationAct));
    //    arrayList.add(new InformacionDron(identificador, "Dron 3", estat, bateria, ultim_manteniment, id_order, 2, punt_inici, punt_desti, locationAct));
    //    arrayList.add(new InformacionDron(identificador, "Dron 4", estat, bateria, ultim_manteniment, id_order, 1, punt_inici, punt_desti, locationAct));
    //    arrayList.add(new InformacionDron(identificador, "Dron 5", estat, bateria, ultim_manteniment, id_order, 2, punt_inici, punt_desti, locationAct));
    //}

    private void getDronePosition(int edge, Context Context) {
        arrayList = new ArrayList<>();
        RequestQueue queue;
        String selectedEdge = "e";
        queue = Volley.newRequestQueue(Context);
        Resources resources = getResources();
        //AIXÒ ES POT MILLORAR
        if(edge==1) {
            selectedEdge = resources.getString(R.string.api_edge1_url);
        }

        else if(edge==0){
            selectedEdge = resources.getString(R.string.api_edge0_url);
        }

        else if(edge==2){
            selectedEdge = resources.getString(R.string.api_edge2_url);
        }
        String url = selectedEdge + "/api/drones_full_info"; // Reemplaza con la URL de tu API
        JSONObject jsonObject = new JSONObject();

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserPref", Context.MODE_PRIVATE);
        String session_Token = sharedPreferences.getString("session_token", "Valor nulo");

        //String sessionToken = login.sessionToken;
        System.out.println(session_Token);

        try {
            jsonObject.put("session_token", session_Token);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.get("result").equals("ok")) {
                                // Maneja la respuesta de la API
                                System.out.println(response);
                                JSONArray droneArray = response.getJSONArray("drones");
                                System.out.println("Numero de drones: " + droneArray.length());
                                for (int i = 0; i < droneArray.length(); i++) {
                                    JSONObject droneObject = droneArray.getJSONObject(i);
                                    int identificador = droneObject.getInt("id_dron");
                                    String autonomia = droneObject.getString("autonomy");
                                    String estat = droneObject.getString("status");
                                    String bateria = droneObject.getString("battery");
                                    String ultim_manteniment = droneObject.getString("last_maintenance_date");
                                    String id_order = droneObject.getString("order_identifier");
                                    int id_beehive = droneObject.getInt("id_beehive");

                                    JSONObject punt_inici = droneObject.getJSONObject("location_in ");
                                    double latitude_inici = punt_inici.getDouble("latitude");
                                    double longitude_inici = punt_inici.getDouble("longitude");

                                    JSONObject punt_desti = droneObject.getJSONObject("location_end");
                                    double latitude_desti = punt_desti.getDouble("latitude");
                                    double longitude_desti = punt_desti.getDouble("longitude");

                                    JSONObject locationAct = droneObject.getJSONObject("location_act");
                                    double latitude = locationAct.getDouble("latitude");
                                    double longitude = locationAct.getDouble("longitude");
                                    System.out.println("Coordenades del drone " + droneObject.getInt("id_dron") + ": "
                                            + latitude + ", " + longitude);

                                    arrayList.add(new InformacionDron(identificador, autonomia, estat, bateria, ultim_manteniment, id_order, id_beehive, punt_inici, punt_desti, locationAct));

                                    //Guarda posicio drons en el array de posicions dels drons

                                }

                                // Después de añadir las coordenadas, crea los marcadores en el mapa

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Maneja el error de la solicitud
                        System.out.println(error);
                    }
                }
        );
        System.out.println();
        queue.add(jsonObjectRequest);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_dron, container, false);

        //Por defecto ordenar por nombre Ascendiente
        opcionSeleccionada = getResources().getStringArray(R.array.sort_options_flotas)[0];
        recyclerView_dron = view.findViewById(R.id.recyclerView_dron);
         //CANVIAR
        getDronePosition(numEdge, getActivity());
        Lista(view);

        return view;
    }

    public void Lista(View view) {
        spinnerSort_dron = view.findViewById(R.id.sp_dron);
        iv_regresar = view.findViewById(R.id.iv_dron_back);

        // Quitar cuando se implemente la llamada a la API - Ubicar método justo despues de obtener los datos de la api
        Creacion_elementos_RecyclerView(arrayList);

        //Adapter para el SPINER
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.sort_options_flotas, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSort_dron.setAdapter(adapter);

        //LISTENERS

        //Listener del Spiner
        spinnerSort_dron.setOnItemSelectedListener(seleccion_spiner);

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
            ordenarArrayList(arrayList);

        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
            // Acciones a realizar cuando no se selecciona ninguna opción del Spinner
        }
    };

    // Ordenación por Nombre
    Comparator<InformacionDron> comparadorNombre = new Comparator<InformacionDron>() {
        @Override
        //Comparar por nombre
        public int compare(InformacionDron t1, InformacionDron t2) {
            //Ascendiente
            if(ordenAscendente)
                return t1.getNombre_dron().compareTo(t2.getNombre_dron());
                //Descendiente
            else
                return t2.getNombre_dron().compareTo(t1.getNombre_dron());
        }

    };

    private void ordenarArrayList(ArrayList<InformacionDron> lista_elementos) {
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

    private void Creacion_elementos_RecyclerView(ArrayList<InformacionDron> lista_elementos) {
        // Creación de LayoutManager que se encarga de la disposición de los elementos del RecyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView_dron.setLayoutManager(layoutManager);

        // Creación del adapter con la nueva lista de elementos búscados
        if (!isAdded()) return;
        DronAdapter dronAdapter = new DronAdapter(getActivity(), lista_elementos, getParentFragmentManager());

        recyclerView_dron.setAdapter(dronAdapter);
    }
}