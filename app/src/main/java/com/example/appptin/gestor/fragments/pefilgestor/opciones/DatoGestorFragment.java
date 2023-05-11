package com.example.appptin.gestor.fragments.pefilgestor.opciones;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.appptin.R;
import com.example.appptin.gestor.fragments.pefilgestor.PerfilGestorFragment;

import java.util.Calendar;


public class DatoGestorFragment extends Fragment {

    private ImageView iv_regresar;
    private EditText et_nombre, et_apellidos,et_pais, et_provincia;
    private Button btn_guardar, btn_fecha;

    private String nombre;
    private View view;

    private Spinner sp_genero;

    private DatePickerDialog datePickerDialog;

    public DatoGestorFragment() {
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
        view = inflater.inflate(R.layout.fragment_dato_gestor, container, false);


        initDatePicker();

        iv_regresar = view.findViewById(R.id.iv_dato_gestor_back);
        et_nombre = view.findViewById(R.id.et_dato_gestor_nombre);
        et_apellidos = view.findViewById(R.id.et_dato_gestor_apellidos);
        btn_guardar = view.findViewById(R.id.btn_dato_gestor_guardar);
        btn_fecha = view.findViewById(R.id.btn_dato_gestor_fecha);

        sp_genero = view.findViewById(R.id.sp_dato_gestor_genero);
        et_pais = view.findViewById(R.id.et_dato_gestor_pais);
        et_provincia = view.findViewById(R.id.et_dato_gestor_ciudad);

        //Asignar valores
        SetGenero();

        //btn_fecha.setText(getTodaysDate());

        //Por defecto botón desactivado
        btn_guardar.setEnabled(false);

        // LISTENERS
        iv_regresar.setOnClickListener(regresar);
        btn_guardar.setOnClickListener(guardar);
        setNombreListener();
        setApellidosListener();
        btn_fecha.setOnClickListener(canviar_fecha);
        setGeneroListener();
        setPaisListener();
        setProvinciaListener();


        return view;
    }

    // EVENTOS
    private View.OnClickListener regresar = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            PerfilGestorFragment perfilGestorFragment = new PerfilGestorFragment();
            //Cambio de Fragment
            fragmentTransaction.replace(R.id.frame_container, perfilGestorFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    };

    private View.OnClickListener guardar = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Toast.makeText(getActivity(), "Canvis desats", Toast.LENGTH_SHORT).show();

        }
    };

    private void setNombreListener() {
        et_nombre.addTextChangedListener(new TextWatcher() {
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

    private void setApellidosListener() {
        et_apellidos.addTextChangedListener(new TextWatcher() {
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

    private View.OnClickListener canviar_fecha = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            btn_guardar.setEnabled(true);
            datePickerDialog.show();

        }
    };

    private void setGeneroListener() {
        sp_genero.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Se ha seleccionado una opción, habilito el botón de guardar cambios
                btn_guardar.setEnabled(true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Acciones a realizar cuando no se selecciona ninguna opción del Spinner
            }
        });
    }

    private void setPaisListener() {
        et_pais.addTextChangedListener(new TextWatcher() {
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
        et_provincia.addTextChangedListener(new TextWatcher() {
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


    // Metodo para información del combo del género
    public void SetGenero() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, new String[]{" ", "Masculí", "Femení"});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_genero.setAdapter(adapter);
        //Evita que se active el evento OnItemSelectedListener del spinner cuando se establece el índice de selección.
        sp_genero.setSelection(0, false);
    }

    // Métodos para el combo de fecha
    private String getTodaysDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = makeDateString(day, month, year);
                btn_fecha.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(getActivity(), style, dateSetListener, year, month, day);
        //datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

    }

    // Formato como se mostrará la fecha de nacimiento
    private String makeDateString(int day, int month, int year) {
        return day + " " + getMonthFormat(month) + " " + year;
    }

    // Formato a mostrá para el mes
    private String getMonthFormat(int month) {
        if (month == 1)
            return "Gener";
        if (month == 2)
            return "Febrer";
        if (month == 3)
            return "Març";
        if (month == 4)
            return "Abril";
        if (month == 5)
            return "Maig";
        if (month == 6)
            return "Juny";
        if (month == 7)
            return "Juliol";
        if (month == 8)
            return "Agost";
        if (month == 9)
            return "Setembre";
        if (month == 10)
            return "Octubre";
        if (month == 11)
            return "Novembre";
        if (month == 12)
            return "Desembre";

        //default should never happen
        return "Gener";

    }
}