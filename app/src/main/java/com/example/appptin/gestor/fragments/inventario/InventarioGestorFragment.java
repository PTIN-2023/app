package com.example.appptin.gestor.fragments.inventario;

import android.app.Activity;
import android.app.DownloadManager;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.appptin.Medicament;
import com.example.appptin.R;
import com.example.appptin.paciente.opciones.ConfigPacienteFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class InventarioGestorFragment extends Fragment {

    View vista;
    SearchView searchView;
    RecyclerView recyclerView;
    ArrayList<MedicamentosClass> arrayList;
    ArrayList<MedicamentosClass> searchList;
    private Button afegir;

    Spinner spinnerSort;
    private String opcionSeleccionada = "";
    boolean ordenAscendente;

    public InventarioGestorFragment() {
        // Constructor vacío requerido para los fragmentos.
    }

    public InventarioGestorFragment(String session_token, Activity gestorActivity) {
        System.out.println("session token inventario: " + session_token);
        System.out.println("activit inventario: " + gestorActivity);

        RequestQueue queue = Volley.newRequestQueue(gestorActivity);
        String apiUrl = "http://147.83.159.195:24105";
        String url = apiUrl + "/api/list_inventory_meds";
        JSONObject jsonObject = new JSONObject();



        try {
            jsonObject.put("session_token", session_token);

//            JSONObject filtre = new JSONObject();
//            filtre.put("meds_per_page", 6);
//            filtre.put("page", 1);
//
//            jsonObject.put("filter", filtre);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        arrayList = new ArrayList<>();
        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response);
                try {
                    String result = response.getString("result");
                    System.out.println("resultat: " + result);
                    if (result.equals("ok")) {
                        JSONArray arraymed = response.getJSONArray("medicines");
                        System.out.println(arraymed);
                        for (int i = 0; i < arraymed.length(); i++) {
                            JSONObject jsonObject = arraymed.getJSONObject(i);

                            // Acceder a los campos del objeto JSON
                            String typeOfAdministration = jsonObject.getString("type_of_administration");
                            String nationalCode = jsonObject.getString("national_code");
                            String form = jsonObject.getString("form");
                            String medName = jsonObject.getString("medicine_name");
                            String URLimage = jsonObject.getString("medicine_image_url");
                            String useType = jsonObject.getString("use_type");
                            double pvp = jsonObject.getDouble("pvp");

                            JSONArray jsonarray_prospecto = jsonObject.getJSONArray("excipients");
                            ArrayList<String> excipients = new ArrayList<String>();
                            for (int j = 0; j < jsonarray_prospecto.length(); j++) {
                                excipients.add(jsonarray_prospecto.getString(j));
                            }

                            boolean prescriptionNeeded = jsonObject.getBoolean("prescription_needed");
                            //String tipusUs = jsonObject.getString("tipus_us");

                            arrayList.add
                                    (new MedicamentosClass(medName, URLimage, nationalCode, useType, typeOfAdministration, prescriptionNeeded, pvp, form, excipients));
                            System.out.println(arrayList);
                        }
                        //Agregar los elementos del RecyclerView
                        Creacion_elementos_RecyclerView(arrayList);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }




            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Manejo de errores de la solicitud
                error.printStackTrace();
            }
        });

        queue.add(jsonArrayRequest);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        vista = inflater.inflate(R.layout.fragment_inventario_gestor, container, false);

        //Por defecto ordenar por nombre Ascendiente de medicamentos
        opcionSeleccionada = getResources().getStringArray(R.array.sort_options_inventario)[0];

        asignar_elementos(vista);

        return vista;
    }

    public void asignar_elementos(View view) {
        //Asociación de los obejtos creados en el XML (diseño)
        recyclerView = view.findViewById(R.id.rv_inventario);
        searchView = view.findViewById(R.id.sv_inventario);
        spinnerSort = view.findViewById(R.id.sp_iventario_gestor_ordenar);
        afegir = view.findViewById(R.id.btn_afegir) ;
        //Agregar los elementos del RecyclerView
        Creacion_elementos_RecyclerView(arrayList);

        //Adapter para el SPINER
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.sort_options_inventario, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSort.setAdapter(adapter);

        //Listener del boton afegir
        afegir.setOnClickListener(abrirADDmed);
        //Listener del Spiner
        spinnerSort.setOnItemSelectedListener(seleccion_spiner);

        //Listener del searView
        searchView.setOnQueryTextListener(buscador);

    }

    private View.OnClickListener abrirADDmed = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            AddMedicamentFragment addMedicamentFragmentFragment = new AddMedicamentFragment();
            transaction.replace(R.id.frame_container, addMedicamentFragmentFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    };
    private void Creacion_elementos_RecyclerView(ArrayList<MedicamentosClass> lista_elementos) {
        // Verificar si el fragment està afegit al FragmentManager
        if (!isAdded()) {
            return;
        }

        // Creación de LayoutManager que se encarga de la disposición de los elementos del RecyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        // Creación del adapter con la nueva lista de elementos búscados
        MedicamentosAdapter medicamentosAdapter = new MedicamentosAdapter(lista_elementos,getActivity());

        recyclerView.setAdapter(medicamentosAdapter);
    }

    //EVENTOS
    private SearchView.OnQueryTextListener buscador = new SearchView.OnQueryTextListener(){
        //Acción al realizar una búsqueda al presionar  el botón de cerca
        @Override
        public boolean onQueryTextSubmit(String query) {
            //Guardar los Objetos de la clase MedicamentoClass relacionados a la búsqueda
            searchList = new ArrayList<>();
            if (query.length() > 0) {
                //Recorrer todos los Objetos (los elementos de la lista)
                for (int i = 0; i < arrayList.size(); i++) {
                    // Comprobar si coincide el texto con algún elemento ya sea por DNI, nombre o apellidos
                    if (arrayList.get(i).getNombre_medicamento().toUpperCase().contains(query.toUpperCase())) {
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

        @Override
        public boolean onQueryTextChange(String newText) {
            //Guardar los Objetos de la clase MedicamentoClass relacionados a la búsqueda
            searchList = new ArrayList<>();
            if (newText.length() > 0) {
                //Recorrer todos los Objetos (los elementos de la lista)
                for (int i = 0; i < arrayList.size(); i++) {
                    // Comprobar si coincide el texto con algún elemento ya sea por DNI, nombre o apellidos
                    if (arrayList.get(i).getNombre_medicamento().toUpperCase().contains(newText.toUpperCase())) {
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

    //Ordenar por nombre medicamento
    Comparator<MedicamentosClass> comparadorNombre = new Comparator<MedicamentosClass>() {
        @Override
        //Comparar por nombre
        public int compare(MedicamentosClass t1, MedicamentosClass t2) {
            //Ascendiente
            if(ordenAscendente)
                return t1.getNombre_medicamento().compareTo(t2.getNombre_medicamento());
                //Descendiente
            else
                return t2.getNombre_medicamento().compareTo(t1.getNombre_medicamento());
        }

    };

    private void ordenarArrayList(ArrayList<MedicamentosClass> lista_elementos) {
        //Lee las opciones del fichero values/arrays.xml
        String[] opciones = getResources().getStringArray(R.array.sort_options_inventario);

        if (opcionSeleccionada.equals(opciones[0])) {
            // Ordenar por nombre ascendente
            ordenAscendente = true;
            if (arrayList != null) Collections.sort(arrayList, comparadorNombre);
            Creacion_elementos_RecyclerView(lista_elementos);

        } else if (opcionSeleccionada.equals(opciones[1])) {
            // Ordenar por nombre descendiente
            ordenAscendente = false;
            if (arrayList != null) Collections.sort(arrayList, comparadorNombre);
            Creacion_elementos_RecyclerView(lista_elementos);
        }
    }
}