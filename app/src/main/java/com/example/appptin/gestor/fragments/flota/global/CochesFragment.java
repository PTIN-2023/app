package com.example.appptin.gestor.fragments.flota.global;

import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.appptin.R;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class CochesFragment extends Fragment {

    View view;
    RecyclerView recyclerView_coches;
    SearchView searchView_coches;
    Spinner spinnerSort_coches;
    private String opcionSeleccionada = "";
    boolean ordenAscendente;

    ArrayList<InformacionCoche> arrayList;
    ArrayList<InformacionCoche> searchList;

    public CochesFragment() {
        // Required empty public constructor

        //Quitar cuando se implemente método para obtener los datos de la api
        arrayList = new ArrayList<>();
        arrayList.add(new InformacionCoche("Coche 1",1));
        arrayList.add(new InformacionCoche("Coche 2",2));
        arrayList.add(new InformacionCoche("Coche 3",1));

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

        Lista(view);

        return view;
    }

    public void Lista(View view) {
        
        searchView_coches = view.findViewById(R.id.searchView_coches);
        spinnerSort_coches = view.findViewById(R.id.sp_coches);

        // Quitar cuando se implemente la llamada a la API - Ubicar método justo despues de obtener los datos de la api
        Creacion_elementos_RecyclerView(arrayList);

        //Adapter para el SPINER
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.sort_options_flotas, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSort_coches.setAdapter(adapter);

        //LISTENERS

        //Listener del Spiner
        spinnerSort_coches.setOnItemSelectedListener(seleccion_spiner);

        //Listener del searView
        searchView_coches.setOnQueryTextListener(buscador);
    }

    private AdapterView.OnItemSelectedListener seleccion_spiner = new AdapterView.OnItemSelectedListener(){

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            opcionSeleccionada = adapterView.getItemAtPosition(i).toString();
            // Limpiar texto del SearhView
            searchView_coches.setQuery("", false);
            ordenarArrayList(arrayList);

        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
            // Acciones a realizar cuando no se selecciona ninguna opción del Spinner
        }
    };

    private SearchView.OnQueryTextListener buscador = new SearchView.OnQueryTextListener(){
        //Acción al realizar una búsqueda al presionar  el botón de cerca
        @Override
        public boolean onQueryTextSubmit(String query) {
            spinnerSort_coches.setEnabled(false);
            //Guardar los Objetos de la clase PeticionClass relacionados a la búsqueda
            searchList = new ArrayList<>();
            //search_peticiones = new ArrayList<>();
            if (query.length() > 0) {
                //Recorrer todos los Objetos (los elementos de la lista)
                for (int i = 0; i < arrayList.size(); i++) {
                    // Comprobar si coincide el texto con algún elemento ya sea por DNI, nombre o apellidos
                    if (arrayList.get(i).getCoche().toUpperCase().contains(query.toUpperCase())){
                        //Afegir element
                        searchList.add(arrayList.get(i));
                    }
                }

                Creacion_elementos_RecyclerView(searchList);
                //Creacion_elementos_RecyclerView(search_peticiones);

            }
            //En caso de no localzarse ningún objeto (elementos) se carga la lista de objetos completos
            else {
                spinnerSort_coches.setEnabled(true);
                Creacion_elementos_RecyclerView(arrayList);
            }
            return false;
        }

        //Acción al cambiar el texto de búsqueda
        @Override
        public boolean onQueryTextChange(String newText) {
            spinnerSort_coches.setEnabled(false);
            searchList = new ArrayList<>();
            if (newText.length() > 0) {
                for (int i = 0; i < arrayList.size(); i++) {
                    if (arrayList.get(i).getCoche().toUpperCase().contains(newText.toUpperCase())){

                        //Afegir element
                        searchList.add(arrayList.get(i));
                    }
                }
                Creacion_elementos_RecyclerView(searchList);

            } else {
                spinnerSort_coches.setEnabled(true);
                Creacion_elementos_RecyclerView(arrayList);
            }
            return false;
        }
    };

    // Ordenación por Nombre
    Comparator<InformacionCoche> comparadorNombre = new Comparator<InformacionCoche>() {
        @Override
        //Comparar por nombre
        public int compare(InformacionCoche t1, InformacionCoche t2) {
            //Ascendiente
            if(ordenAscendente)
                return t1.getCoche().compareTo(t2.getCoche());
                //Descendiente
            else
                return t2.getCoche().compareTo(t1.getCoche());
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
}