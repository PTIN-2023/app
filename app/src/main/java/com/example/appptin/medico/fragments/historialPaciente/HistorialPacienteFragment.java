package com.example.appptin.medico.fragments.historialPaciente;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.appptin.R;


public class HistorialPacienteFragment extends Fragment {
    TextView tv_medico, tv_nombre, tv_apellido, tv_dni, tv_fecha, tv_genero
            ,tv_cip, tv_pais, tv_provincia, tv_antecedente, tv_problema, tv_tratamiento;

    InformePaciente informePaciente;
    public HistorialPacienteFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getParentFragmentManager().setFragmentResultListener("key_aprobar_peticion", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                informePaciente = (InformePaciente) result.getSerializable("MiObjeto");

                //Asignar valores
                tv_medico.setText(informePaciente.getMedico());
                tv_nombre.setText(informePaciente.getNombre());
                tv_apellido.setText(informePaciente.getApellidos());
                tv_dni.setText(informePaciente.getDni());
                tv_fecha.setText(informePaciente.getFecha());
                tv_genero.setText(informePaciente.getGenero());
                tv_cip.setText(informePaciente.getCip());
                tv_pais.setText(informePaciente.getPais());
                tv_provincia.setText(informePaciente.getProvincia());
                tv_antecedente.setText(informePaciente.getAntecedente());
                tv_problema.setText(informePaciente.getProblema());
                tv_tratamiento.setText(informePaciente.getTratamiento());

            }
        });

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_historial_paciente, container, false);

        // Asociar elementos del layout
        tv_medico = view.findViewById(R.id.txt_informe_medico);
        tv_nombre = view.findViewById(R.id.txt_informe_nombre);
        tv_apellido = view.findViewById(R.id.txt_informe_apellido);
        tv_dni = view.findViewById(R.id.txt_informe_dni);
        tv_fecha = view.findViewById(R.id.txt_informe_fecha);
        tv_genero = view.findViewById(R.id.txt_informe_genero);
        tv_cip = view.findViewById(R.id.txt_informe_cip);
        tv_pais = view.findViewById(R.id.txt_informe_pais);
        tv_provincia = view.findViewById(R.id.txt_informe_provincia);
        tv_antecedente = view.findViewById(R.id.txt_informe_antecedente);
        tv_problema = view.findViewById(R.id.txt_informe_problema);
        tv_tratamiento = view.findViewById(R.id.txt_informe_tratamiento);

        return view;
    }
}