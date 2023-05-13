package com.example.appptin.medico.fragments.perfilmedico.opciones;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;

import com.example.appptin.MainActivity;
import com.example.appptin.R;
import com.example.appptin.medico.MedicoActivity;
import com.example.appptin.medico.fragments.perfilmedico.PerfilMedicoFragment;
import com.example.appptin.paciente.UserFragment;
import com.example.appptin.paciente.opciones.ConfigPacienteFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ConfigMedicoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConfigMedicoFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private ImageView iv_regresar;
    private View view;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ConfigMedicoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ConfigMedicoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ConfigMedicoFragment newInstance(String param1, String param2) {
        ConfigMedicoFragment fragment = new ConfigMedicoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_config_medico, container, false);

        iv_regresar = view.findViewById(R.id.iv_config_medico_back);

        // LISTENERS
        iv_regresar.setOnClickListener(regresar);



        final MedicoActivity ma = (MedicoActivity) getActivity();
        SharedPreferences sp = ma.getSharedPreferences("SP", ma.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sp.edit();
        final Switch swi = view.findViewById(R.id.switchTema);

        //Aquí gestionamos que el switch vaya acompasado
        int theme = sp.getInt("Theme",1);
        if(theme==1){
            swi.setChecked(false);
        }
        else{
            swi.setChecked(true);
        }
        //Aquí ambiamos el tema y hacemos que el usuario recuerde al inciar la aplicación
        swi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(swi.isChecked()){
                    editor.putInt("Theme",0);
                }
                else {
                    editor.putInt("Theme",1);
                }
                editor.commit();
                ma.setDayNight();
            }
        });

        return view;
    }

    private View.OnClickListener regresar = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            PerfilMedicoFragment perfilMedicoFragment = new PerfilMedicoFragment();
            //Cambio de Fragment
            fragmentTransaction.replace(R.id.frame_container, perfilMedicoFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    };

}