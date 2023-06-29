package com.example.appptin.gestor.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appptin.R;
import com.example.appptin.gestor.fragments.EdgeMapaCiudadAdapter;
import com.example.appptin.gestor.fragments.EdgeMapaCiudadFragment;
import com.example.appptin.gestor.fragments.flota.local.InformacionCiudadEdge;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class EdgeMapaCiudadFragment extends Fragment {

    private View view;
    private RecyclerView recyclerView_ciudad;
    private ImageView iv_regresar;
    private Spinner spinnerSort_ciudad;
    private String opcionSeleccionada = "";
    boolean ordenAscendente;

    ArrayList<InformacionCiudadEdge> arrayList;
    ArrayList<InformacionCiudadEdge> searchList;
    public EdgeMapaCiudadFragment() {
        //Quitar cuando se implemente método para obtener los datos de la api
        arrayList = new ArrayList<>();
        arrayList.add(new InformacionCiudadEdge("Vilanova i la Geltrú"));
        arrayList.add(new InformacionCiudadEdge("Barcelona"));
        arrayList.add(new InformacionCiudadEdge("Cubelles"));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_edge_ciudad, container, false);

        //Por defecto ordenar por nombre Ascendiente
        opcionSeleccionada = getResources().getStringArray(R.array.sort_options_flotas)[0];
        recyclerView_ciudad = view.findViewById(R.id.recyclerView_ciudad);

        Lista(view);

        return view;
    }

    public void Lista(View view) {

        spinnerSort_ciudad = view.findViewById(R.id.sp_ciudades);
        iv_regresar = view.findViewById(R.id.iv_edge_ciudad_back);

        // Quitar cuando se implemente la llamada a la API - Ubicar método justo despues de obtener los datos de la api
        Creacion_elementos_RecyclerView(arrayList);

        //Adapter para el SPINER
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.sort_options_flotas, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSort_ciudad.setAdapter(adapter);

        //LISTENERS

        //Listener del Spiner
        spinnerSort_ciudad.setOnItemSelectedListener(seleccion_spiner);

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
    Comparator<InformacionCiudadEdge> comparadorNombre = new Comparator<InformacionCiudadEdge>() {
        @Override
        //Comparar por nombre
        public int compare(InformacionCiudadEdge t1, InformacionCiudadEdge t2) {
            //Ascendiente
            if(ordenAscendente)
                return t1.getCiudad().compareTo(t2.getCiudad());
                //Descendiente
            else
                return t2.getCiudad().compareTo(t1.getCiudad());
        }

    };

    private void ordenarArrayList(ArrayList<InformacionCiudadEdge> lista_elementos) {
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

    private void Creacion_elementos_RecyclerView(ArrayList<InformacionCiudadEdge> lista_elementos) {
        // Creación de LayoutManager que se encarga de la disposición de los elementos del RecyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView_ciudad.setLayoutManager(layoutManager);

        // Creación del adapter con la nueva lista de elementos búscados
        if (!isAdded()) return;
        EdgeMapaCiudadAdapter edgeMapaCiudadAdapter = new EdgeMapaCiudadAdapter(getActivity(), lista_elementos, getParentFragmentManager());

        recyclerView_ciudad.setAdapter(edgeMapaCiudadAdapter);
    }

}
