package com.example.appptin.paciente.opciones;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.appptin.R;
import com.example.appptin.medico.fragments.perfilmedico.PerfilMedicoFragment;

public class ClienteDireccionFragment extends Fragment {
    private ImageView iv_regresar;
    private Button btn_guardar;
    private EditText edt_direccion, edt_opcional, edt_localidad, edt_provincia, edt_cp, edt_pais;
    private View view;
    public ClienteDireccionFragment() {
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
        view =  inflater.inflate(R.layout.fragment_cliente_direccion, container, false);

        iv_regresar = view.findViewById(R.id.iv_cliente_direccion_back);
        btn_guardar = view.findViewById(R.id.btn_cliente_direccion_guardar);
        edt_direccion = view.findViewById(R.id.et_cliente_direccion);
        edt_opcional = view.findViewById(R.id.et_cliente_direccion_opcional);
        edt_localidad = view.findViewById(R.id.et_cliente_direccion_localidad);
        edt_provincia = view.findViewById(R.id.et_cliente_direccion_provincia);
        edt_cp = view.findViewById(R.id.et_cliente_direccion_cp);
        edt_pais = view.findViewById(R.id.et_cliente_direccion_pais);

        //Por defecto botón guardar desactivado hasta que se edite algún campo
        btn_guardar.setEnabled(false);

        // LISTENERS
        iv_regresar.setOnClickListener(regresar);
        btn_guardar.setOnClickListener(guardar);
        setDireccionListener();
        setOpcionalListener();
        setLocalidadListener();
        setProvinciaListener();
        setCodiPostalListener();
        setPaisListener();



        return view;
    }

    private View.OnClickListener regresar = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            if (fragmentManager.getBackStackEntryCount() > 0) {
                // Retrocede en la pila de fragmentos
                fragmentManager.popBackStack();
            }
        }
    };

    private View.OnClickListener guardar = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Toast.makeText(getActivity(),"Canvis desats",Toast.LENGTH_SHORT).show();

        }
    };

    private void setDireccionListener() {
        edt_direccion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                btn_guardar.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void setOpcionalListener() {
        edt_opcional.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                btn_guardar.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void setLocalidadListener() {
        edt_localidad.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                btn_guardar.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void setProvinciaListener() {
        edt_provincia.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                btn_guardar.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void setCodiPostalListener() {
        edt_cp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                btn_guardar.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void setPaisListener() {
        edt_pais.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                btn_guardar.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }
}