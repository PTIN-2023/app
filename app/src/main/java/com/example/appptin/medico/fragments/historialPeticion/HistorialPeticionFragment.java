package com.example.appptin.medico.fragments.historialPeticion;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.appptin.medico.MedicoActivity;
import com.example.appptin.Medicament;
import com.example.appptin.R;
import com.example.appptin.login;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class HistorialPeticionFragment extends Fragment {
    private static final String TAG = "HistorialPeticionFragment";

    // Variables necesarias
    View view;
    SearchView searchView;
    RecyclerView recyclerView;
    ArrayList<InformacionPeticion> arrayList;
    ArrayList<InformacionPeticion> searchList;
    //ArrayList<InformacionPeticion> total_peticiones;
    //ArrayList<InformacionPeticion> search_peticiones;
    TextView titulo;
    String campo;

    // Codigo para diferenciar entre los diferentes fragments
    int codi;
    int posicion;
    Context cont;

    Spinner spinnerSort;
    boolean ordenAscendente;
    private String opcionSeleccionada = "";
    public HistorialPeticionFragment() {
        // Required empty public constructor
    }
    public HistorialPeticionFragment(String texto, int codigo,int posicion, Context context) throws IOException {
        this.campo = texto;
        this.codi = codigo;
        this.posicion = posicion;

        this.cont = context;

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

        recyclerView = view.findViewById(R.id.recyclerView);
        arrayList = new ArrayList<>();

        //Llamadas a la api
        api_llamada();

        // Creación de los elementos del diseño
        Lista(view);

        return view;
    }
    public void Lista(View view) {
        //Asociación de los obejtos creados en el XML (diseño)
        //recyclerView = view.findViewById(R.id.recyclerView);
        searchView = view.findViewById(R.id.searchView);
        titulo = view.findViewById(R.id.textView_historial);
        spinnerSort = view.findViewById(R.id.sp_historial_peticion_ordenar);

        titulo.setText(campo);

        //Agregar los elementos del RecyclerView
        //Creacion_elementos_RecyclerView(arrayList);

        //Adapter para el SPINER
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.sort_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSort.setAdapter(adapter);

        //Listener del Spiner
        spinnerSort.setOnItemSelectedListener(seleccion_spiner);

        //Listener del searView
        searchView.setOnQueryTextListener(buscador);

    }
    private void Creacion_elementos_RecyclerView(ArrayList<InformacionPeticion> lista_elementos) {
        // Creación de LayoutManager que se encarga de la disposición de los elementos del RecyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        // Creación del adapter con la nueva lista de elementos búscados
        if (!isAdded()) return;
        PeticionAdapter peticionAdapter = new PeticionAdapter(getActivity(), lista_elementos, codi, getChildFragmentManager());

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
            //search_peticiones = new ArrayList<>();
            if (query.length() > 0) {
                //Recorrer todos los Objetos (los elementos de la lista)
                for (int i = 0; i < arrayList.size(); i++) {
                    // Comprobar si coincide el texto con algún elemento ya sea por DNI, nombre o apellidos
                    if (arrayList.get(i).getPatient_fullname().toUpperCase().contains(query.toUpperCase())
                            || arrayList.get(i).getDate().toUpperCase().contains(query.toUpperCase())){


                        //Afegir element
                        searchList.add(arrayList.get(i));
                    }
                }

                Creacion_elementos_RecyclerView(searchList);
                //Creacion_elementos_RecyclerView(search_peticiones);

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
                    if (arrayList.get(i).getPatient_fullname().toUpperCase().contains(newText.toUpperCase())
                            || arrayList.get(i).getDate().toUpperCase().contains(newText.toUpperCase())){

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
    Comparator<InformacionPeticion> comparadorNombre = new Comparator<InformacionPeticion>() {
        @Override
        //Comparar por nombre
        public int compare(InformacionPeticion t1, InformacionPeticion t2) {
            //Ascendiente
            if(ordenAscendente)
                return t1.getPatient_fullname().compareTo(t2.getPatient_fullname());
            //Descendiente
            else
                return t2.getPatient_fullname().compareTo(t1.getPatient_fullname());
        }

    };

    // Comparador para las fechas
    Comparator<InformacionPeticion> comparadorFecha = new Comparator<InformacionPeticion>() {
        @Override
        public int compare(InformacionPeticion t1, InformacionPeticion t2) {
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date fecha1 = dateFormat.parse(t1.getDate());
                Date fecha2 = dateFormat.parse(t2.getDate());

                // Ascendente
                if (ordenAscendente) {
                    return fecha1.compareTo(fecha2);
                } else {
                    // Descendente
                    return fecha2.compareTo(fecha1);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return 0;
        }
    };


    private void ordenarArrayList(ArrayList<InformacionPeticion> lista_elementos) {
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
        } else if (opcionSeleccionada.equals(opciones[2])) {
            // Ordenar por fecha ascendente
            ordenAscendente = true;
            Collections.sort(arrayList, comparadorFecha);
            Creacion_elementos_RecyclerView(lista_elementos);
        } else if (opcionSeleccionada.equals(opciones[3])) {
            // Ordenar por fecha descendente
            ordenAscendente = false;
            Collections.sort(arrayList, comparadorFecha);
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

        SharedPreferences sharedPreferences = this.cont.getSharedPreferences("UserPref", Context.MODE_PRIVATE);

        // Datos enviados
        try {
            //jsonBody.put("session_token", login.getSession_token());
            jsonBody.put("session_token", sharedPreferences.getString("session_token", "No value"));
            jsonBody.put("confirmations_per_page", 4);
            jsonBody.put("page", 1);
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
                        // Listado de peticiones que el doctor ha de confirmar
                        JSONArray ordersArray = response.getJSONArray("orders");
                        if(ordersArray.length()>0) {
                            //Guardar los datos obtenidos
                            for (int i = 0; i < ordersArray.length(); i++) {
                                // Obtener el Objeto
                                JSONObject peticionObject = ordersArray.getJSONObject(i);

                                // Obtener la información del Objeto
                                String order_identifier = peticionObject.getString("order_identifier");
                                String date = peticionObject.getString("date");
                                String patient_fullname = peticionObject.getString("patient_full_name");

                                InformacionPeticion informacion_cliente = new InformacionPeticion(order_identifier, date, patient_fullname);

                                // Lista de medicamentos que han de ser confirmados
                                JSONArray medicine_list_Array = peticionObject.getJSONArray("medicine_list");

                                // Guardar los valores de los medicamentos del cliente
                                for (int j = 0; j < medicine_list_Array.length(); j++) {
                                    JSONArray medicineArray = medicine_list_Array.getJSONArray(j);
                                    String nationalCode = medicineArray.getString(0);
                                    String medicine_name = medicineArray.getString(1);

                                    //System.out.println("Código: " + nationalCode + ", Nombre: " + medicine_name);

                                    Medicament medicament = new Medicament(nationalCode, medicine_name);
                                    // Guardar medicamento
                                    informacion_cliente.setMedicine_list(medicament);
                                }

                                arrayList.add(informacion_cliente);
                            }
                        }
                        else {
                            // Mensaje por pantalla
                            //aviso("No tens peticions assignades");
                            Toast.makeText(getActivity(),"No tens peticions assignades",Toast.LENGTH_SHORT).show();
                        }

                        Creacion_elementos_RecyclerView(arrayList);

                    }

                    // Caso no exitoso, crear algún dialogo
                    else{
                        System.out.println("No se han leído los datos en la api");
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
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserPref", Context.MODE_PRIVATE);

        // Datos enviados
        try {
            jsonBody.put("session_token", sharedPreferences.getString("session_token", "No value"));
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
                try {
                    String result = response.getString("result");
                    ArrayList<InformacionPeticion> arrayList = new ArrayList<>();
                    // Caso exitoso
                    if (result.equals("ok")) {
                        // Listado de peticiones que el doctor ha de confirmar
                        JSONArray ordersArray = response.getJSONArray("orders");
                        if(ordersArray.length()>0) {
                            //Guardar los datos obtenidos
                            for (int i = 0; i < ordersArray.length(); i++) {
                                // Obtener el Objeto
                                JSONObject peticionObject = ordersArray.getJSONObject(i);

                                // Obtener la información del Objeto
                                String order_identifier = peticionObject.getString("order_identifier");
                                String date = peticionObject.getString("date");
                                String patient_fullname = peticionObject.getString("patient_full_name");
                                //String approved = peticionObject.getString("approved");

                                InformacionPeticion informacion_cliente = new InformacionPeticion(order_identifier,date,patient_fullname);

                                // Lista de medicamentos que han de ser confirmados
                                JSONArray medicine_list_Array = peticionObject.getJSONArray("medicine_list");

                                // Guardar los valores de los medicamentos del cliente
                                for (int j = 0; j < medicine_list_Array.length(); j++) {
                                    JSONArray medicineArray = medicine_list_Array.getJSONArray(j);
                                    String nationalCode = medicineArray.getString(0);
                                    String medicine_name = medicineArray.getString(1);

                                    //System.out.println("Código: " + nationalCode + ", Nombre: " + medicine_name);

                                    Medicament medicament = new Medicament(nationalCode,medicine_name);
                                    // Guardar medicamento
                                    informacion_cliente.setMedicine_list(medicament);
                                }

                                arrayList.add(informacion_cliente);
                            }
                        }
                        else {
                            // Mensaje por pantalla
                            //aviso("No has validat cap petició");
                            Toast.makeText(getActivity(),"No has validat cap petició",Toast.LENGTH_SHORT).show();
                        }

                        Creacion_elementos_RecyclerView(arrayList);

                    }

                    // Caso no exitoso, crear algún dialogo
                    else{
                        System.out.println("No se han leído los datos en la api");
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