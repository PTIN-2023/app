package com.example.appptin.medico.fragments.perfilmedico;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.appptin.MainActivity;
import com.example.appptin.R;
import com.example.appptin.login;
import com.example.appptin.medico.fragments.perfilmedico.opciones.DatoMedicoFragment;

import de.hdodenhof.circleimageview.CircleImageView;

public class PerfilMedicoFragment extends Fragment {
    CircleImageView foto_perfil;
    RelativeLayout rl_salir, rl_dades;

    public PerfilMedicoFragment() {
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
        View view = inflater.inflate(R.layout.fragment_perfil_medico, container, false);

        foto_perfil = view.findViewById(R.id.civ_perfil_medico_foto);
        foto_perfil.setImageResource(R.drawable.avatar_medico);
        rl_dades = view.findViewById(R.id.rl_perfil_medico_datos);
        rl_salir = view.findViewById(R.id.rl_perfil_medico_salir);

        // Asignar Listeners
        rl_salir.setOnClickListener(salirClickListener);
        rl_dades.setOnClickListener(abrirFragmentDatos);

        return view;
    }

    private View.OnClickListener salirClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Toast.makeText(getActivity(),"Sessió tancada",Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(getActivity(), login.class);
            intent.putExtra("mensaje",1);
            startActivity(intent);
        }
    };

    private View.OnClickListener abrirFragmentDatos = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            DatoMedicoFragment datosMedicoFragment = new DatoMedicoFragment();
            transaction.replace(R.id.frame_container, datosMedicoFragment);
            transaction.commit();

        }
    };


}