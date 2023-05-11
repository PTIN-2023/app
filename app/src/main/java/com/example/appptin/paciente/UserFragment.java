package com.example.appptin.paciente;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.appptin.R;
import com.example.appptin.welcome_page;
import com.example.appptin.paciente.opciones.DatoPacienteFragment;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserFragment extends Fragment {
    private CircleImageView foto_perfil;
    private RelativeLayout rl_salir, rl_dades;

    public UserFragment() {
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

        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();

        View view = inflater.inflate(R.layout.fragment_user, container, false);

        foto_perfil = view.findViewById(R.id.profile_image);
        foto_perfil.setImageResource(R.drawable.foto_perfil_usuario);
        rl_dades = view.findViewById(R.id.rl_perfil_usuario_datos);
        rl_salir = view.findViewById(R.id.rl_perfil_usuario_salir);

        // Asignar Listeners
        rl_salir.setOnClickListener(salirClickListener);
        rl_dades.setOnClickListener(abrirFragmentDatos);

        return view;
    }

    private View.OnClickListener salirClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Toast.makeText(getActivity(),"Sessió tancada",Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(getActivity(), welcome_page.class);
            startActivity(intent);
        }
    };

    private View.OnClickListener abrirFragmentDatos = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
           FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            DatoPacienteFragment datoPacienteFragment = new DatoPacienteFragment();
            transaction.replace(R.id.frameLayout, datoPacienteFragment);
            transaction.commit();
        }
    };

}