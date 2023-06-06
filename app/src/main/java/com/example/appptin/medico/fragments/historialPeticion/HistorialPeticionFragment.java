package com.example.appptin.medico.fragments.historialPeticion;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.appptin.Medicament;
import com.example.appptin.R;
import com.example.appptin.login;
import com.example.appptin.medico.conexion.Conexion_json;
import com.example.appptin.medico.conexion.InformacionBase;
import com.example.appptin.welcome_page;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class HistorialPeticionFragment extends Fragment {
    private static final String TAG = "HistorialPeticionFragment";

    // Variables necesarias
    View view;
    SearchView searchView;
    RecyclerView recyclerView;
    ArrayList<InformacionBase> arrayList;
    ArrayList<InformacionBase> searchList;
    TextView titulo;
    String campo;

    // Codigo para diferenciar entre los diferentes fragments
    int codi;
    int posicion;
    Context cont;
    Spinner spinnerSort;
    boolean ordenAscendente;
    private String opcionSeleccionada = "";

    public HistorialPeticionFragment(String texto, int codigo,int posicion, Context context) throws IOException {
        this.campo = texto;
        this.codi = codigo;
        this.posicion = posicion;

        arrayList = new ArrayList<>();

        this.cont = context;

        //Leer datos de Json
        conexion();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_historial_peticion, container, false);

        //Por defecto ordenar por nombre Ascendiente
        opcionSeleccionada = getResources().getStringArray(R.array.sort_options)[0];

        //Llamadas a la api
        api_llamada();

        // Creación de los elementos del diseño
        Lista(view);

        return view;
    }

    public void Lista(View view) {
        //Asociación de los obejtos creados en el XML (diseño)
        recyclerView = view.findViewById(R.id.recyclerView);
        searchView = view.findViewById(R.id.searchView);
        titulo = view.findViewById(R.id.textView_historial);
        spinnerSort = view.findViewById(R.id.sp_historial_peticion_ordenar);

        titulo.setText(campo);

        //Agregar los elementos del RecyclerView
        Creacion_elementos_RecyclerView(arrayList);

        //Adapter para el SPINER
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.sort_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSort.setAdapter(adapter);

        //Listener del Spiner
        spinnerSort.setOnItemSelectedListener(seleccion_spiner);

        //Listener del searView
        searchView.setOnQueryTextListener(buscador);

    }

    public void conexion(){
        //Envío el contexto
        Conexion_json con = new Conexion_json(this.cont);

        String Filename="";
        if(codi == 1 || codi == 2) Filename = "peticions_per_aprovar.json";
        else if (codi == 3) Filename = "informe_paciente.json";

        //Creo la conexión
        String jsonString = con.readJsonFromFile(Filename);

        //Obtener lista de elementos
        //ArrayList<PeticionClass> peticionList = con.getPedidosFromJson(jsonString);
        if(codi == 1 || codi == 2)  arrayList  = con.getPedidosFromJson(jsonString);
        else if ( codi ==3) arrayList  = con.getInformePacientesFromJson(jsonString);

    }

    private void Creacion_elementos_RecyclerView(ArrayList<InformacionBase> lista_elementos ){
        //Creación de LayoutManager que se encarga de la disposición de los elementos del RecyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        // Creación del adapter con la nueva lista de elementos búscados
        PeticionAdapter peticionAdapter = new PeticionAdapter(getActivity(), lista_elementos,codi,getParentFragmentManager());

        recyclerView.setAdapter(peticionAdapter);

    }

    //EVENTOS
    private SearchView.OnQueryTextListener buscador = new SearchView.OnQueryTextListener(){
        //Acción al realizar una búsqueda al presionar  el botón de cerca
        @Override
        public boolean onQueryTextSubmit(String query) {
            spinnerSort.setEnabled(false);
            //Guardar los Objetos de la clase PeticionClass relacionados a la búsqueda
            searchList = new ArrayList<>();
            if (query.length() > 0) {
                //Recorrer todos los Objetos (los elementos de la lista)
                for (int i = 0; i < arrayList.size(); i++) {
                    // Comprobar si coincide el texto con algún elemento ya sea por DNI, nombre o apellidos
                    if (arrayList.get(i).getDni().toUpperCase().contains(query.toUpperCase())
                            || arrayList.get(i).getNombre().toUpperCase().contains(query.toUpperCase())
                            || arrayList.get(i).getApellidos().toUpperCase().contains(query.toUpperCase())) {

                        //Afegir element
                        searchList.add(arrayList.get(i));
                    }
                }

                Creacion_elementos_RecyclerView(searchList);

            }
            //En caso de no localzarse ningún objeto (elementos) se carga la lista de objetos completos
            else {
                spinnerSort.setEnabled(true);
                Creacion_elementos_RecyclerView(arrayList);
            }
            return false;
        }

        //Acción al cambiar el texto de búsqueda
        @Override
        public boolean onQueryTextChange(String newText) {
            spinnerSort.setEnabled(false);
            searchList = new ArrayList<>();
            if (newText.length() > 0) {
                for (int i = 0; i < arrayList.size(); i++) {
                    if (arrayList.get(i).getDni().toUpperCase().contains(newText.toUpperCase())
                            || arrayList.get(i).getNombre().toUpperCase().contains(newText.toUpperCase())
                            || arrayList.get(i).getApellidos().toUpperCase().contains(newText.toUpperCase())) {
                        //Afegir element
                        searchList.add(arrayList.get(i));
                    }
                }
                Creacion_elementos_RecyclerView(searchList);

            } else {
                spinnerSort.setEnabled(true);
                Creacion_elementos_RecyclerView(arrayList);
            }
            return false;
        }
    };


    private AdapterView.OnItemSelectedListener seleccion_spiner = new AdapterView.OnItemSelectedListener(){

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            opcionSeleccionada = adapterView.getItemAtPosition(i).toString();
            // Limpiar texto del SearhView
            searchView.setQuery("", false);
            ordenarArrayList(arrayList);

        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
            // Acciones a realizar cuando no se selecciona ninguna opción del Spinner
        }
    };

    // Ordenación por Nombre
    Comparator<InformacionBase> comparadorNombre = new Comparator<InformacionBase>() {
        @Override
        //Comparar por nombre
        public int compare(InformacionBase t1, InformacionBase t2) {
            //Ascendiente
            if(ordenAscendente)
                return t1.getNombre().compareTo(t2.getNombre());
                //Descendiente
            else
                return t2.getNombre().compareTo(t1.getNombre());
        }

    };

    private void ordenarArrayList(ArrayList<InformacionBase> lista_elementos) {
        //Lee las opciones del fichero values/arrays.xml
        String[] opciones = getResources().getStringArray(R.array.sort_options);

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

    // API
    private void api_llamada(){

        // Información de las peticiones por confirmar
        if (codi == 1) api_informacion_peticiones_aprobar();
        // Historial de peticiones
        else if (codi == 2) api_informacion_historial_peticiones();
    }

    private void api_informacion_peticiones_aprobar(){

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        Resources r = getResources();
        String apiUrl = r.getString(R.string.api_base_url);
        String url = apiUrl + "/api/list_doctor_pending_confirmations";
        JSONObject jsonBody = new JSONObject();

        // Datos enviados
        try {
            jsonBody.put("session_token", login.getSession_token());
            jsonBody.put("confirmations_per_page", 5);
            jsonBody.put("page", 1);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Datos devueltos
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    System.out.println("MENSAJE: " + response);
                 }
             }, new Response.ErrorListener() {
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

        queue.add(jsonObjectRequest);

    }
    private void api_informacion_historial_peticiones(){

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        Resources r = getResources();
        String apiUrl = r.getString(R.string.api_base_url);
        String url = apiUrl + "/api/list_doctor_approved_confirmations";
        JSONObject jsonBody = new JSONObject();

        // Datos enviados
        try {
            jsonBody.put("session_token", login.getSession_token());
            jsonBody.put("confirmations_per_page", 5);
            jsonBody.put("page", 1);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Datos devueltos
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println("MENSAJE: " + response);
            }
        }, new Response.ErrorListener() {
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

        queue.add(jsonObjectRequest);

    }
}