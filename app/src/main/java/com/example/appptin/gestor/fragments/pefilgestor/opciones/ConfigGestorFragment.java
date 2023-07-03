package com.example.appptin.gestor.fragments.pefilgestor.opciones;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;

import com.example.appptin.R;
import com.example.appptin.gestor.GestorActivity;
import com.example.appptin.gestor.fragments.pefilgestor.PerfilGestorFragment;

import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ConfigGestorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConfigGestorFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ImageView iv_regresar;
    private View view;

    private Spinner sp_idioma;
    private LanguageChangeListener mListener;

    private String selectedLanguage;

    public ConfigGestorFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ConfigGestorFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ConfigGestorFragment newInstance(String param1, String param2) {
        ConfigGestorFragment fragment = new ConfigGestorFragment();
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
        view =  inflater.inflate(R.layout.fragment_config_gestor, container, false);

        iv_regresar = view.findViewById(R.id.iv_config_gestor_back);
        sp_idioma = view.findViewById(R.id.sp_gestor_idioma);
        final Spinner sp_theme = view.findViewById(R.id.sp_switch); // Nuestro spinner de tema

        // LISTENERS
        iv_regresar.setOnClickListener(regresar);

        SetIdioma();
        cambiar_idioma();

        final GestorActivity ga = (GestorActivity) getActivity();
        SharedPreferences sp = ga.getSharedPreferences("SP", ga.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sp.edit();

        // Establecer el adaptador del spinner del tema
        ArrayAdapter<String> themeAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, new String[]{"Light", "Dark"});
        themeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_theme.setAdapter(themeAdapter);

        //Aquí gestionamos que el spinner vaya acompasado
        int theme = sp.getInt("Theme",1);
        if(theme==1){
            sp_theme.setSelection(0); // Light
        }
        else{
            sp_theme.setSelection(1); // Dark
        }

        //Aquí cambiamos el tema y hacemos que el usuario recuerde al iniciar la aplicación
        sp_theme.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0) { // Light
                    editor.putInt("Theme",1);
                } else { // Dark
                    editor.putInt("Theme",0);
                }
                editor.commit();
                ga.setDayNight();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Acciones a realizar cuando no se selecciona ninguna opción del Spinner
            }
        });

        return view;
    }

    private View.OnClickListener regresar = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            FragmentManager fragmentManager = getActivity().getSupportFragmentManager(); // Si estás en un Fragment, utiliza getFragmentManager()
            if (fragmentManager.getBackStackEntryCount() > 0) {
                // Retrocede en la pila de fragmentos
                fragmentManager.popBackStack();
            }
        }
    };


    public interface LanguageChangeListener {
        void onLanguageChanged();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof LanguageChangeListener) {
            mListener = (LanguageChangeListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement LanguageChangeListener");
        }
    }

    private void cambiar_idioma() {
        sp_idioma.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Se ha seleccionado una opción, habilito el botón de guardar cambios
                selectedLanguage = parent.getItemAtPosition(position).toString();
                // Actualiza la configuración del idioma en el contexto de la aplicación
                Resources res = getResources();
                Configuration config = res.getConfiguration();
                Locale locale = new Locale(selectedLanguage);
                Locale.setDefault(locale);
                config.setLocale(locale);
                res.updateConfiguration(config, res.getDisplayMetrics());
                if (mListener != null) {
                    mListener.onLanguageChanged();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Acciones a realizar cuando no se selecciona ninguna opción del Spinner
            }
        });
    }

    public void SetIdioma() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, new String[]{" ", "cat", "es"});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_idioma.setAdapter(adapter);
        //Evita que se active el evento OnItemSelectedListener del spinner cuando se establece el índice de selección.
        sp_idioma.setSelection(0, false);
    }

}