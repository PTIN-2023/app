package com.example.appptin.gestor.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.appptin.R;
import com.example.appptin.gestor.fragments.flota.global.CochesFragment;
import com.example.appptin.gestor.fragments.flota.local.EdgeCiudadFragment;
import com.example.appptin.gestor.fragments.inventario.MapaGestor;

public class MapaFragment extends Fragment {

    Button btn_global, btn_local, btn_usuario;
    public MapaFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_mapa, container, false);

        btn_global = v.findViewById(R.id.button_global);
        btn_local = v.findViewById(R.id.button_local);
        btn_usuario = v.findViewById(R.id.button_crear_usuario);

        //Listener
        btn_global.setOnClickListener(listener_global);
        btn_local.setOnClickListener(listener_local);
        btn_usuario.setOnClickListener(listener_usuario);
        return v;
    }

    // EVENTOS
    private View.OnClickListener listener_global = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getActivity(), MapaGestor.class);
            startActivity(intent);
            //FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            //CochesFragment cochesFragment = new CochesFragment();
            //transaction.replace(R.id.frame_container, cochesFragment);
            //transaction.addToBackStack(null);
            //transaction.commit();

        }
    };

    private View.OnClickListener listener_local = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            EdgeMapaCiudadFragment edgeMapaCiudadFragment = new EdgeMapaCiudadFragment();
            transaction.replace(R.id.frame_container, edgeMapaCiudadFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    };

    private View.OnClickListener listener_usuario = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            CrearUsersFragment crearUsersFragment = new CrearUsersFragment();
            transaction.replace(R.id.frame_container, crearUsersFragment);
            transaction.addToBackStack(null);
            transaction.commit();

        }
    };
}
