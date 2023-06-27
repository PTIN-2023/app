package com.example.appptin.gestor.fragments.pefilgestor.opciones;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.appptin.R;
import com.example.appptin.gestor.fragments.pefilgestor.PerfilGestorFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;


public class DatoGestorFragment extends Fragment {

    private ImageView iv_regresar;
    private EditText et_nombre, et_apellidos,et_pais, et_provincia;
    private Button btn_guardar, btn_fecha;

    private String nombre;
    private View view;

    private Spinner sp_genero;

    private DatePickerDialog datePickerDialog;

    private EditText et_user_given_name, et_user_full_name, et_city, et_address , et_email;

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

        //Campos pantalla
        iv_regresar = view.findViewById(R.id.iv_dato_paciente_back);
        et_user_given_name = view.findViewById(R.id.et_user_given_name);
        et_user_full_name = view.findViewById(R.id.et_user_name);
        et_email = view.findViewById(R.id.et_email);
        et_city = view.findViewById(R.id.et_user_city);
        et_address = view.findViewById(R.id.et_user_address);;
        btn_guardar = view.findViewById(R.id.btn_dato_paciente_guardar);

        Context context = this.getContext();
        // Obtiene las SharedPreferences
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserPref", Context.MODE_PRIVATE);

        // Obtiene los valores de las SharedPreferences
        et_user_given_name.setText(sharedPreferences.getString("user_given_name", "Valor vacio"));
        et_user_full_name.setText(sharedPreferences.getString("user_full_name", "Valor vacio"));
        et_email.setText(sharedPreferences.getString("user_email", "Valor vacio"));
        et_city.setText(sharedPreferences.getString("user_city", "Valor vacio"));
        et_address.setText(sharedPreferences.getString("user_address", "Valor vacio"));

        //Asignar valores
        //SetGenero();

        //btn_fecha.setText(getTodaysDate());

        //Por defecto botón activado
        btn_guardar.setEnabled(true);
        btn_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveInfo(v);
            }
        });

        // LISTENERS
        iv_regresar.setOnClickListener(regresar);
        setNombreListener();
        //btn_fecha.setOnClickListener(canviar_fecha);
        //setGeneroListener();
        //setPaisListener();
        //setProvinciaListener();


        return view;
    }

    // EVENTOS
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

    private View.OnClickListener guardar = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Toast.makeText(getActivity(), "Canvis desats", Toast.LENGTH_SHORT).show();

        }
    };

    private void setNombreListener() {
        et_user_given_name.addTextChangedListener(new TextWatcher() {
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

    public void saveInfo(View view) {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        Resources r = getResources();
        String apiUrl = r.getString(R.string.api_base_url);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserPref", Context.MODE_PRIVATE);

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("token", sharedPreferences.getString("session_token", "No value"));
            jsonBody.put("user_full_name", et_user_given_name.getText());
            jsonBody.put("user_given_name", et_user_full_name.getText());
            jsonBody.put("user_email", et_email.getText());
            jsonBody.put("user_phone", "609078022");
            jsonBody.put("user_city", et_city.getText());
            jsonBody.put("user_address", et_address.getText());
            //jsonBody.put("user_password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url = r.getString(R.string.api_base_url) + "/api/set_user_info"; // Reemplaza con la dirección de tu API
        System.out.println(url);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String result = response.getString("result");
                            System.out.println(result);
                            if (result.equals("ok")) {

                                Toast.makeText(getActivity(), "Les dades s'han modificat correctament.", Toast.LENGTH_LONG).show();
                                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserPref", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();

                                editor.putString("user_full_name", et_user_full_name.getText().toString());
                                editor.putString("user_given_name", et_user_given_name.getText().toString());
                                editor.putString("user_city", et_city.getText().toString());
                                editor.putString("user_address", et_address.getText().toString());


                                editor.apply();
                            } else {
                                Toast.makeText(getActivity(), "Hi ha hagut un error en la solicitut.", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Error al realizar la solicitud
                        error.printStackTrace();
                    }
                });

        // Añade la solicitud a la cola de solicitudes
        queue.add(jsonObjectRequest);
    }
}