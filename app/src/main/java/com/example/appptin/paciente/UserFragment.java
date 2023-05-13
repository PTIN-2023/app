package com.example.appptin.paciente;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.appptin.R;
import com.example.appptin.paciente.opciones.ClienteDireccionFragment;
import com.example.appptin.paciente.opciones.ConfigPacienteFragment;
import com.example.appptin.welcome_page;
import com.example.appptin.paciente.opciones.DatosPacienteFragment;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserFragment extends Fragment {
    private CircleImageView foto_perfil;
    private RelativeLayout rl_salir, rl_dades, rl_config, rl_direccion;
    private Patient patient;
    Button editar_foto;
    View view;
    private static final int REQUEST_CODE = 1;
    private static final int RESULT_OK = -1;

    private static final String ARG_PATIENT= "patient";

    public UserFragment() {
        // Required empty public constructor
    }

    public static UserFragment newInstance(Patient patient) {
        UserFragment fragment = new UserFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PATIENT, patient);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            patient = (Patient) getArguments().getSerializable(ARG_PATIENT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();

        view = inflater.inflate(R.layout.fragment_user, container, false);

        foto_perfil = view.findViewById(R.id.profile_image);
        foto_perfil.setImageResource(R.drawable.foto_perfil_usuario);  //asignación de imagen por defecto
        rl_dades = view.findViewById(R.id.rl_perfil_usuario_datos);
        rl_salir = view.findViewById(R.id.rl_perfil_usuario_salir);
        rl_config = view.findViewById(R.id.rl_perfil_usuario_config);
        rl_direccion = view.findViewById(R.id.rl_perfil_usuario_direccion);
        editar_foto = view.findViewById(R.id.btn_perfil_user_foto);

        // Asignar Listeners
        rl_salir.setOnClickListener(salirClickListener);
        rl_dades.setOnClickListener(abrirFragmentDatos);
        rl_config.setOnClickListener(abrirFragmentConfig);
        rl_direccion.setOnClickListener(abrirFragmentDireccion);
        editar_foto.setOnClickListener(asignarImagen);

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
            DatosPacienteFragment datosPacienteFragment = DatosPacienteFragment.newInstance(patient);
            transaction.replace(R.id.frameLayout, datosPacienteFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    };
    private View.OnClickListener abrirFragmentConfig = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            ConfigPacienteFragment configPacienteFragment = new ConfigPacienteFragment();
            transaction.replace(R.id.frameLayout, configPacienteFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    };

    private View.OnClickListener abrirFragmentDireccion = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            ClienteDireccionFragment clienteDireccionFragment = new ClienteDireccionFragment();
            transaction.replace(R.id.frameLayout, clienteDireccionFragment);
            transaction.addToBackStack(null);
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
            CircleImageView imageView = view.findViewById(R.id.profile_image);
            imageView.setImageURI(imageUri);
        }
    }

}