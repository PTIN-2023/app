package com.example.appptin.gestor.fragments.flota.local.drons;

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
import com.example.appptin.gestor.fragments.flota.global.CocheAdapter;
import com.example.appptin.gestor.fragments.flota.global.InformacionCoche;
import com.example.appptin.gestor.fragments.flota.local.InformacionCiudadEdge;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class DronFragment extends Fragment {

    View view;
    RecyclerView recyclerView_dron;
    SearchView searchView_dron;
    Spinner spinnerSort_dron;
    private String opcionSeleccionada = "";
    boolean ordenAscendente;

    ArrayList<InformacionDron> arrayList;
    ArrayList<InformacionDron> searchList;
    public DronFragment() {
        //Quitar cuando se implemente método para obtener los datos de la api
        arrayList = new ArrayList<>();
        arrayList.add(new InformacionDron("Dron 1",1));
        arrayList.add(new InformacionDron("Dron 2",1));
        arrayList.add(new InformacionDron("Dron 3",2));
        arrayList.add(new InformacionDron("Dron 4",1));
        arrayList.add(new InformacionDron("Dron 5",2));
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
        recyclerView_dron = view.findViewById(R.id.recyclerView_coches);

        Lista(view);

        return view;
    }

    public void Lista(View view) {

        searchView_dron = view.findViewById(R.id.searchView_dron);
        spinnerSort_dron = view.findViewById(R.id.sp_dron);

        // Quitar cuando se implemente la llamada a la API - Ubicar método justo despues de obtener los datos de la api
        Creacion_elementos_RecyclerView(arrayList);

        //Adapter para el SPINER
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.sort_options_flotas, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSort_dron.setAdapter(adapter);

        //LISTENERS

        //Listener del Spiner
        spinnerSort_dron.setOnItemSelectedListener(seleccion_spiner);

        //Listener del searView
        searchView_dron.setOnQueryTextListener(buscador);
    }

    private AdapterView.OnItemSelectedListener seleccion_spiner = new AdapterView.OnItemSelectedListener(){

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            opcionSeleccionada = adapterView.getItemAtPosition(i).toString();
            // Limpiar texto del SearhView
            searchView_dron.setQuery("", false);
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
            spinnerSort_dron.setEnabled(false);
            //Guardar los Objetos de la clase PeticionClass relacionados a la búsqueda
            searchList = new ArrayList<>();
            //search_peticiones = new ArrayList<>();
            if (query.length() > 0) {
                //Recorrer todos los Objetos (los elementos de la lista)
                for (int i = 0; i < arrayList.size(); i++) {
                    if (arrayList.get(i).getNombre_dron().toUpperCase().contains(query.toUpperCase())){
                        //Afegir element
                        searchList.add(arrayList.get(i));
                    }
                }

                Creacion_elementos_RecyclerView(searchList);
                //Creacion_elementos_RecyclerView(search_peticiones);

            }
            //En caso de no localzarse ningún objeto (elementos) se carga la lista de objetos completos
            else {
                spinnerSort_dron.setEnabled(true);
                Creacion_elementos_RecyclerView(arrayList);
            }
            return false;
        }

        //Acción al cambiar el texto de búsqueda
        @Override
        public boolean onQueryTextChange(String newText) {
            spinnerSort_dron.setEnabled(false);
            searchList = new ArrayList<>();
            if (newText.length() > 0) {
                for (int i = 0; i < arrayList.size(); i++) {
                    if (arrayList.get(i).getNombre_dron().toUpperCase().contains(newText.toUpperCase())){

                        //Afegir element
                        searchList.add(arrayList.get(i));
                    }
                }
                Creacion_elementos_RecyclerView(searchList);

            } else {
                spinnerSort_dron.setEnabled(true);
                Creacion_elementos_RecyclerView(arrayList);
            }
            return false;
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