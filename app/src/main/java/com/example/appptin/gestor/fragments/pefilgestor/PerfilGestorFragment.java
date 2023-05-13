package com.example.appptin.gestor.fragments.pefilgestor;

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

import com.example.appptin.R;
import com.example.appptin.gestor.fragments.pefilgestor.opciones.ConfigGestorFragment;
import com.example.appptin.gestor.fragments.pefilgestor.opciones.DatoGestorFragment;
import com.example.appptin.login;
import com.example.appptin.medico.fragments.perfilmedico.opciones.ConfigMedicoFragment;

import de.hdodenhof.circleimageview.CircleImageView;


public class PerfilGestorFragment extends Fragment {

    CircleImageView foto_perfil;

    RelativeLayout rl_salir, rl_dades, rl_config;

    public PerfilGestorFragment() {
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
        View view = inflater.inflate(R.layout.fragment_perfil_gestor, container, false);

        foto_perfil = view.findViewById(R.id.civ_perfil_gestor_foto);
        foto_perfil.setImageResource(R.drawable.avatar_gestor);
        rl_dades = view.findViewById(R.id.rl_perfil_gestor_datos);
        rl_salir = view.findViewById(R.id.rl_perfil_gestor_salir);
        rl_config = view.findViewById(R.id.rl_perfil_gestor_config);
        // Asignar Listeners
        rl_salir.setOnClickListener(salirClickListener);
        rl_dades.setOnClickListener(abrirFragmentDatos);
        rl_config.setOnClickListener(abrirFragmentConfig);

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
            DatoGestorFragment datosGestorFragment = new DatoGestorFragment();
            transaction.replace(R.id.frame_container, datosGestorFragment);
            transaction.commit();

        }
    };

    private View.OnClickListener abrirFragmentConfig = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            ConfigGestorFragment configGestorFragment = new ConfigGestorFragment();
            transaction.replace(R.id.frame_container, configGestorFragment);
            transaction.commit();
        }
    };

}