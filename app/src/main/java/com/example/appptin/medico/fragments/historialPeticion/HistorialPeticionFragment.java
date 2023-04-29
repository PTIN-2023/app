package com.example.appptin.medico.fragments.historialPeticion;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.appptin.R;
import com.example.appptin.medico.conexion.Conexion_json;
import com.example.appptin.medico.conexion.InformacionBase;

import java.io.IOException;
import java.util.ArrayList;

public class HistorialPeticionFragment extends Fragment {

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
    boolean borrar;
    Context cont;

    public HistorialPeticionFragment(String texto, int codigo,int posicion,boolean borrar, Context context) throws IOException {
        this.campo = texto;
        this.codi = codigo;
        this.posicion = posicion;
        this.borrar = borrar;

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
        Lista(view);

        return view;
    }

    public void Lista(View view) {
        //Asociación de los obejtos creados en el XML (diseño)
        recyclerView = view.findViewById(R.id.recyclerView);
        searchView = view.findViewById(R.id.searchView);
        titulo = view.findViewById(R.id.textView_historial);

        titulo.setText(campo);


        //Creación de LayoutManager que se encarga de la disposición de los elementos del RecyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        //Asociación de adapter que se encarga de adaptar los datos a lo que finalmente verá el usuario.
        // Es el encargado de traducir datos en UI.
        PeticionAdapter peticionAdapter = new PeticionAdapter(getActivity(),arrayList,codi,getParentFragmentManager());
        recyclerView.setAdapter(peticionAdapter);

        //Listener del searView
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            //Acción al realizar una búsqueda al presionar  el botón de cerca
            @Override
            public boolean onQueryTextSubmit(String query) {
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
                    //Creación de LayoutManager que se encarga de la disposición de los elementos del RecyclerView
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                    recyclerView.setLayoutManager(layoutManager);

                    // Creación del adapter con la nueva lista de elementos búscados
                    PeticionAdapter peticionAdapter = new PeticionAdapter(getActivity(), searchList,codi,getParentFragmentManager());

                    recyclerView.setAdapter(peticionAdapter);
                }
                //En caso de no localzarse ningún objeto (elementos) se carga la lista de objetos completos
                else {
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                    recyclerView.setLayoutManager(layoutManager);

                    PeticionAdapter peticionAdapter = new PeticionAdapter(getActivity(), arrayList,codi,getParentFragmentManager());
                    recyclerView.setAdapter(peticionAdapter);
                }
                return false;
            }

            //Acción al cambiar el texto de búsqueda
            @Override
            public boolean onQueryTextChange(String newText) {
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
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                    recyclerView.setLayoutManager(layoutManager);

                    PeticionAdapter peticionAdapter = new PeticionAdapter(getActivity(), searchList,codi,getParentFragmentManager());

                    recyclerView.setAdapter(peticionAdapter);
                } else {
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                    recyclerView.setLayoutManager(layoutManager);

                    PeticionAdapter peticionAdapter = new PeticionAdapter(getActivity(), arrayList,codi,getParentFragmentManager());

                    recyclerView.setAdapter(peticionAdapter);
                }
                return false;
            }
        });

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
        //arrayList  = con.getPedidosFromJson(jsonString);

        //Recorrer lista
        /*for (int j = 0; j < peticionList.size(); j++) {
            Log.d("JSON", "Nombre: " + peticionList.get(j).toString());
        }
        */

    }

}