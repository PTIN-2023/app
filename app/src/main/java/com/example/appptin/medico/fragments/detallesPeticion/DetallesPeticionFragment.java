package com.example.appptin.medico.fragments.detallesPeticion;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.appptin.R;
import com.example.appptin.medico.fragments.historialPeticion.PeticionClass;

import java.util.ArrayList;

public class DetallesPeticionFragment extends Fragment {
    ArrayList<String> medicamentos = new ArrayList<String>();
    TextView textView_nombre, textView_apellido, textView_dni, textView_pedido;
    FragmentManager activity;
    Context context;
    PeticionClass peticion;

    public DetallesPeticionFragment(FragmentManager activity, Context context) {
        // Required empty public constructor
        this.activity = activity;
        this.context = context;
    }

    /*public DetallesPeticionFragment() {
        medicamentos = new ArrayList<>();
        medicamentos.add("Actafarma Oxicol 28caps");
        medicamentos.add("Cicatridina Supositorios 10uds");
        medicamentos.add("Aquilea Omega 3 Forte 90caps");
        medicamentos.add("Formag Magnesio Marino 90comp");
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Obtener resultados recibidos de otro Fragment
        getParentFragmentManager().setFragmentResultListener("key_aprobar_peticion", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {

                peticion = (PeticionClass) result.getSerializable("MiObjeto");

                Log.d("JSON", peticion.toString());

                textView_nombre.setText(peticion.getNombre());
                textView_apellido.setText(peticion.getApellidos());
                textView_dni.setText(peticion.getDni());
                textView_pedido.setText(peticion.getNumeroPedido());

                for (int i = 0; i < peticion.getMedicamentosSize(); i++) {
                    medicamentos.add(peticion.getMedicamentos().get(i));
                }

            }
        });

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_detalles_peticion, container, false);

        // Asociar elementos del layout
        textView_nombre = view.findViewById(R.id.txt_nombre_historial);
        textView_apellido = view.findViewById(R.id.txt_apellido_historial);
        textView_dni = view.findViewById(R.id.txt_dni_historial);
        textView_pedido = view.findViewById(R.id.txt_detalles_pedido);

        AdaptadorListView(view);
        return view;
    }


    public void AdaptadorListView(View view){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, medicamentos);
        ListView listView = (ListView) view.findViewById(R.id.ListView_Peticion);
        listView.setAdapter(adapter);
    }
}


