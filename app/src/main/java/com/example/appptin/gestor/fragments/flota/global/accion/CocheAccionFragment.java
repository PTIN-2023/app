package com.example.appptin.gestor.fragments.flota.global.accion;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appptin.Medicament;
import com.example.appptin.R;
import com.example.appptin.gestor.fragments.flota.global.CochesFragment;
import com.example.appptin.gestor.fragments.flota.global.InformacionCoche;
import com.example.appptin.medico.fragments.historialPeticion.InformacionPeticion;

import org.json.JSONException;

import java.util.Map;


public class CocheAccionFragment extends Fragment {

    private Button btn_info, btn_bateria, btn_fallada;
    private TextView txt_titulo;
    private ImageView iv_regresar;
    private View view;
    private InformacionCoche peticion;
    public CocheAccionFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_coche_accion, container, false);

        iv_regresar = view.findViewById(R.id.iv_coche_accion_back);
        txt_titulo = view.findViewById(R.id.txt_coche_accion_titulo);
        btn_info = view.findViewById(R.id.btn_coche_accion_info);
        btn_bateria = view.findViewById(R.id.btn_coche_accion_bateria);
        btn_fallada = view.findViewById(R.id.btn_coche_accion_fallada);

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
        getParentFragmentManager().setFragmentResultListener("key_info_coche", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {

                peticion = (InformacionCoche) result.getSerializable("Info_coche");
                txt_titulo.setText(txt_titulo.getText().toString()+" "+peticion.getCoche());

            }
        });
    }

    // EVENTOS
    private View.OnClickListener listener_info = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Dialogo_info();

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

    private void Dialogo_info(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Información coche");

        StringBuilder message = new StringBuilder();
        message.append("Identificador: ").append(peticion.getIdentificador()).append("\n");
        message.append("Matrícula: ").append(peticion.getMatricula()).append("\n");
        message.append("Estat: ").append(peticion.getEstat()).append("\n");
        message.append("Batería: ").append(peticion.getBateria()).append("\n");
        message.append("Último mantenimiento: ").append(peticion.getUltim_manteniment()).append("\n");
        message.append("Paquetes: ").append("\n");
        if (peticion.getPaquets() != null && peticion.getPaquets().length() > 0) {
            for (int i = 0; i < peticion.getPaquets().length(); i++) {
                try {
                    String paquet = peticion.getPaquets().getString(i);
                    message.append("   - ").append(paquet).append("\n");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } else {
            message.append("   No hay paquets disponibles").append("\n");
        }
        message.append("Posición actual:").append("\n");
        message.append("    Latitud: ").append(peticion.getLatitude()).append("\n");
        message.append("    Longitud: ").append(peticion.getLongitude()).append("\n");

        builder.setMessage(message.toString());
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Acciones al hacer clic en el botón Aceptar
            }
        });

        builder.show();
    }
}