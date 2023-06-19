package com.example.appptin.medico.fragments.detallesPeticion;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.appptin.Medicament;
import com.example.appptin.R;
import com.example.appptin.medico.fragments.aprobarPeticion.MedicamentosAdapter;
import com.example.appptin.medico.fragments.historialPeticion.InformacionPeticion;
import com.example.appptin.medico.fragments.historialPeticion.PeticionClass;

import java.util.ArrayList;
import java.util.Map;

public class DetallesPeticionFragment extends Fragment {
    ArrayList<Medicament> medicamentos = new ArrayList<Medicament>();
    TextView textView_nombre, textView_apellido, textView_dni, textView_pedido;
    FragmentManager activity;
    Context context;
    InformacionPeticion informacionPeticion;
    RecyclerView recyclerView;

    public DetallesPeticionFragment(FragmentManager activity, Context context) {
        this.activity = activity;
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getParentFragmentManager().setFragmentResultListener("key_aprobar_peticion", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                informacionPeticion = (InformacionPeticion) result.getSerializable("MiObjeto");
                informacionPeticion = (InformacionPeticion) result.getSerializable("MiObjeto");

                // Log.d("JSON", peticion.toString());

                textView_nombre.setText(informacionPeticion.getPatient_fullname());
                textView_dni.setText(informacionPeticion.getDate());
                textView_pedido.setText(informacionPeticion.getOrder_identifier());


                Map<String, Medicament> list_medicament = informacionPeticion.getMedicine_list();

                for (Map.Entry<String, Medicament> entry : list_medicament.entrySet()) {
                    String key = entry.getKey();
                    Medicament value = entry.getValue();
                    System.out.println("Clave: " + key + ", Valor: " + value);
                    //Creo un medicamento con su nombre y cantidad
                    String cantidadString = String.valueOf(entry.getValue().getCantidad());
                    int cantidad = Integer.parseInt(cantidadString);

                    Medicament medicament = new Medicament(entry.getValue().getMedName(), cantidad);


                    medicamentos.add(medicament);
                }


            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detalles_peticion, container, false);

        textView_nombre = view.findViewById(R.id.txt_nombre_historial);
        textView_apellido = view.findViewById(R.id.txt_apellido_historial);
        textView_dni = view.findViewById(R.id.txt_dni_historial);
        textView_pedido = view.findViewById(R.id.txt_detalles_pedido);

        Lista_medicamentos(view);
        return view;
    }

    public void Lista_medicamentos(View view) {
        //Asociación de los objetos creados en el XML (diseño)
        recyclerView = view.findViewById(R.id.ListView_Peticion);

        textView_nombre = view.findViewById(R.id.txt_nombre_historial);
        textView_dni = view.findViewById(R.id.txt_dni_historial);
        textView_pedido = view.findViewById(R.id.txt_detalles_pedido);

        //Creación de LayoutManager que se encarga de la disposición de los elementos del RecyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        //Asociación de adapter que se encarga de adaptar los datos a lo que finalmente verá el usuario.
        // Es el encargado de traducir datos en UI.
        MedicamentosAdapter medicamentosAdapter = new MedicamentosAdapter(getActivity(), medicamentos);
        recyclerView.setAdapter(medicamentosAdapter);
    }
}



