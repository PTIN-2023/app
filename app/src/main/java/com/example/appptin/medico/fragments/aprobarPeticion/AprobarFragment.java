package com.example.appptin.medico.fragments.aprobarPeticion;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appptin.R;
import com.example.appptin.medico.conexion.Conexion_json;
import com.example.appptin.medico.fragments.historialPeticion.HistorialPeticionFragment;
import com.example.appptin.medico.fragments.historialPeticion.PeticionClass;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;


public class AprobarFragment extends Fragment {
    RecyclerView recyclerView;
    ArrayList<String> medicamentos = new ArrayList<String>();
    TextView textView_nombre, textView_apellido, textView_dni, textView_pedido;
    Button btn_aceptar, btn_rechazar;
    int posicion;
    FragmentManager activity;
    PeticionClass peticion;
    Context context;

    public AprobarFragment(FragmentManager activity, Context context) {
        // Required empty public constructor
        this.activity = activity;
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_aprobar, container, false);
        Lista_medicamentos(view);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Obtener resultados recibidos de otro Fragment
        getParentFragmentManager().setFragmentResultListener("key_aprobar_peticion", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {

                peticion = (PeticionClass) result.getSerializable("MiObjeto");

                // Log.d("JSON", peticion.toString());

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

    public void Lista_medicamentos(View view) {
        //Asociación de los objetos creados en el XML (diseño)
        recyclerView = view.findViewById(R.id.recyclerView_aprobar);

        textView_nombre = view.findViewById(R.id.txt_peticion_nombre);
        textView_apellido = view.findViewById(R.id.txt_peticion_apellido);
        textView_dni = view.findViewById(R.id.txt_peticion_dni);
        textView_pedido = view.findViewById(R.id.txt_peticion_pedido);

        btn_aceptar = view.findViewById(R.id.btn_aceptar_peticion);
        btn_rechazar = view.findViewById(R.id.btn_rechazar_peticion);


        //Creación de LayoutManager que se encarga de la disposición de los elementos del RecyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        //Asociación de adapter que se encarga de adaptar los datos a lo que finalmente verá el usuario.
        // Es el encargado de traducir datos en UI.
        MedicamentosAdapter medicamentosAdapter = new MedicamentosAdapter(getActivity(), medicamentos);
        recyclerView.setAdapter(medicamentosAdapter);

        // Se acepta la petición del paciente
        btn_aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Petició aceptada", Toast.LENGTH_SHORT).show();

                // Eliminar Objeto del json

              /*  try {
                    Eliminar_Peticio();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                */
                //Fragment anterior
                HistorialPeticionFragment peticionFragment = null;
                try {
                    peticionFragment = new HistorialPeticionFragment("Peticions per aprovar", 1, posicion, true,context);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                //Regresar al Fragment anterior
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                //Cambio de Fragment
                fragmentTransaction.replace(R.id.frame_container, peticionFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        btn_rechazar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Petició Rebutjada", Toast.LENGTH_SHORT).show();

                //Fragment anterior
                HistorialPeticionFragment peticionFragment = null;
                try {
                    peticionFragment = new HistorialPeticionFragment("Peticions per aprovar", 3, posicion, true,context);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                //Regresar al Fragment anterior
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                //Cambio de Fragment
                fragmentTransaction.replace(R.id.frame_container, peticionFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

    }
    public void Eliminar_Peticio() throws JSONException, IOException {

        //Envío el contexto
        Conexion_json con = new Conexion_json(context);

        String fichero = "peticions_per_aprovar.json";
        //Creo la conexión
        String jsonString = con.readJsonFromFile(fichero);

        //Eliminar el objeto
        con.deleteObjectJson(jsonString,peticion.getId(),fichero);

    }
}
