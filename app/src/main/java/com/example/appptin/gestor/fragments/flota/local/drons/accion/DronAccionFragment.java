package com.example.appptin.gestor.fragments.flota.local.drons.accion;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appptin.R;
import com.example.appptin.gestor.fragments.flota.global.InformacionCoche;
import com.example.appptin.gestor.fragments.flota.local.drons.InformacionDron;


public class DronAccionFragment extends Fragment {

    private Button btn_info, btn_bateria, btn_fallada;
    private TextView txt_titulo;
    private ImageView iv_regresar;
    private View view;
    private InformacionDron peticion;
    public DronAccionFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_dron_accion, container, false);

        iv_regresar = view.findViewById(R.id.iv_dron_accion_back);
        txt_titulo = view.findViewById(R.id.txt_dron_accion_titulo);
        btn_info = view.findViewById(R.id.btn_dron_accion_info);
        btn_bateria = view.findViewById(R.id.btn_dron_accion_bateria);
        btn_fallada = view.findViewById(R.id.btn_dron_accion_fallada);

        //Listener
        iv_regresar.setOnClickListener(regresar);
        btn_info.setOnClickListener(listener_info);
        btn_bateria.setOnClickListener(listener_bateria);
        btn_fallada.setOnClickListener(listener_fallada);

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


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Obtener resultados recibidos del Fragment principal (Historial peticion)
        getParentFragmentManager().setFragmentResultListener("key_info_dron", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {

                peticion = (InformacionDron) result.getSerializable("Info_dron");
                txt_titulo.setText(txt_titulo.getText().toString()+" "+peticion.getNombre_dron());

            }
        });
    }


    // EVENTOS
    private View.OnClickListener listener_info = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Toast.makeText(getContext(),"No implementado 1",Toast.LENGTH_SHORT).show();

        }
    };

    private View.OnClickListener listener_bateria = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Toast.makeText(getContext(),"No implementado 2",Toast.LENGTH_SHORT).show();
        }
    };

    private View.OnClickListener listener_recollida = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Toast.makeText(getContext(),"No implementado 3",Toast.LENGTH_SHORT).show();
        }
    };

    private View.OnClickListener listener_paquet = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Toast.makeText(getContext(),"No implementado 4",Toast.LENGTH_SHORT).show();
        }
    };

    private View.OnClickListener listener_fallada = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Toast.makeText(getContext(),"No implementado 5",Toast.LENGTH_SHORT).show();
        }
    };
}