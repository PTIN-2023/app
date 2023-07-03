package com.example.appptin.medico.fragments.recetas.historial;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.appptin.R;
import com.example.appptin.gestor.fragments.flota.global.CocheAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class HistorialRecetaFragment extends Fragment {

    private View view;
    private ImageView iv_regresar;
    private RecyclerView recyclerView_receta;
    private String email;

    ArrayList<InformacionPreinscripciones> arrayList;
    public HistorialRecetaFragment(ArrayList<InformacionPreinscripciones>  inf_recetas) {
        // Required empty public constructor
        this.arrayList = inf_recetas;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_historial_receta, container, false);;
        recyclerView_receta = view.findViewById(R.id.recyclerView_historial_receta);

        Lista(view);

        return view;
    }

    public void Lista(View view) {

        iv_regresar = view.findViewById(R.id.iv_historial_receta_back);

        // Quitar cuando se implemente la llamada a la API - Ubicar método justo despues de obtener los datos de la api
        Creacion_elementos_RecyclerView(arrayList);

        //LISTENERS
        iv_regresar.setOnClickListener(regresar);
    }

    private View.OnClickListener regresar = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            anterior_ventana();
        }
    };

    private void Creacion_elementos_RecyclerView(ArrayList<InformacionPreinscripciones> lista_elementos) {
        // Creación de LayoutManager que se encarga de la disposición de los elementos del RecyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView_receta.setLayoutManager(layoutManager);

        // Creación del adapter con la nueva lista de elementos búscados
        if (!isAdded()) return;
        HistorialRecetaAdapter historialRecetaAdapter = new HistorialRecetaAdapter(getActivity(), lista_elementos, getParentFragmentManager());

        recyclerView_receta.setAdapter(historialRecetaAdapter);
    }

    private void anterior_ventana(){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager(); // Si estás en un Fragment, utiliza getFragmentManager()
        if (fragmentManager.getBackStackEntryCount() > 0) {
            // Retrocede en la pila de fragmentos
            fragmentManager.popBackStack();
        }
    }
}