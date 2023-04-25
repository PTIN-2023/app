package com.example.appptin;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserFragment extends Fragment {

    CircleImageView foto_perfil;
    Button cerrar_sesion;
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

        foto_perfil = view.findViewById(R.id.imageView_profile_medico);
        foto_perfil.setImageResource(R.drawable.foto_perfil_usuario);

        cerrar_sesion = view.findViewById(R.id.btn_cerrar_sesion_medico);

        //Aqui deberá llamar al Activity de login
        cerrar_sesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(),"Sessió tancada",Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getActivity(), login.class);
                intent.putExtra("mensaje",1);
                startActivity(intent);
            }
        });

        return view;
    }

}