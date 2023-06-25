package com.example.appptin.medico.fragments.recetaPaciente;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.appptin.R;
import com.google.android.material.textfield.TextInputEditText;


public class RecetaFragment extends Fragment {

    EditText nom_pacient, nom_medicament, duracio;
    TextInputEditText notes;
    Button guardar;
    public RecetaFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recepta, container, false);

        // Asociar elementos del layout
        nom_pacient = view.findViewById(R.id.et_receta_paciente);
        nom_medicament = view.findViewById(R.id.et_receta_medicamento);
        duracio = view.findViewById(R.id.et_receta_duracion);
        notes = view.findViewById(R.id.txt_receta_notas);
        guardar = view.findViewById(R.id.btn_recepta_guardar);

        return view;
    }
}