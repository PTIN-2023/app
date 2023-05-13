package com.example.appptin.gestor.fragments.pefilgestor;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

    Button editar_foto;
    View view;
    private static final int REQUEST_CODE = 1;
    private static final int RESULT_OK = -1;

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
        view = inflater.inflate(R.layout.fragment_perfil_gestor, container, false);

        foto_perfil = view.findViewById(R.id.civ_perfil_gestor_foto);
        foto_perfil.setImageResource(R.drawable.avatar_gestor); //asignación de imagen por defecto
        editar_foto = view.findViewById(R.id.btn_perfil_gestor_foto);
        rl_dades = view.findViewById(R.id.rl_perfil_gestor_datos);
        rl_salir = view.findViewById(R.id.rl_perfil_gestor_salir);
        rl_config = view.findViewById(R.id.rl_perfil_gestor_config);

        // Asignar Listeners
        rl_salir.setOnClickListener(salirClickListener);
        rl_dades.setOnClickListener(abrirFragmentDatos);
        rl_config.setOnClickListener(abrirFragmentConfig);
        editar_foto.setOnClickListener(asignarImagen);

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

    private View.OnClickListener asignarImagen = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, REQUEST_CODE);
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();
            CircleImageView imageView = view.findViewById(R.id.civ_perfil_gestor_foto);
            imageView.setImageURI(imageUri);
        }
    }

}