package com.example.appptin.gestor.fragments.inventario;

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
import android.widget.TextView;

import com.example.appptin.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class InventarioGestorFragment extends Fragment {

    View vista;
    SearchView searchView;
    RecyclerView recyclerView;
    TextView titulo;
    ArrayList<MedicamentosClass> arrayList;
    ArrayList<MedicamentosClass> searchList;

    Spinner spinnerSort;
    private String opcionSeleccionada = "";
    boolean ordenAscendente;

    public InventarioGestorFragment() {
        //Prueba
        arrayList = new ArrayList<>();
        arrayList.add(new MedicamentosClass("Medicament 1","red"));
        arrayList.add(new MedicamentosClass("Medicament 2","green"));
        arrayList.add(new MedicamentosClass("Medicament 3","green"));
        arrayList.add(new MedicamentosClass("Medicament 4","red"));
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
        titulo = view.findViewById(R.id.txt_titol_inventario);
        spinnerSort = view.findViewById(R.id.sp_iventario_gestor_ordenar);

        titulo.setText("Inventari");

        //Agregar los elementos del RecyclerView
        Creacion_elementos_RecyclerView(arrayList);

        //Adapter para el SPINER
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.sort_options_inventario, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSort.setAdapter(adapter);

        //Listener del Spiner
        spinnerSort.setOnItemSelectedListener(seleccion_spiner);

        //Listener del searView
        searchView.setOnQueryTextListener(buscador);

    }
    private void Creacion_elementos_RecyclerView(ArrayList<MedicamentosClass> lista_elementos ){
        //Creación de LayoutManager que se encarga de la disposición de los elementos del RecyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        // Creación del adapter con la nueva lista de elementos búscados
        MedicamentosAdapter medicamentosAdapter = new MedicamentosAdapter(getActivity(), lista_elementos,getParentFragmentManager());

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
            Collections.sort(arrayList, comparadorNombre);
            Creacion_elementos_RecyclerView(lista_elementos);

        } else if (opcionSeleccionada.equals(opciones[1])) {
            // Ordenar por nombre descendiente
            ordenAscendente = false;
            Collections.sort(arrayList, comparadorNombre);
            Creacion_elementos_RecyclerView(lista_elementos);
        }
    }
}