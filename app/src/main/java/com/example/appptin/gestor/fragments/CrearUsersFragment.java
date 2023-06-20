package com.example.appptin.gestor.fragments;

import android.content.res.Resources;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.appptin.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CrearUsersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CrearUsersFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private EditText userFullName, userGivenName, userEmail, userPhone, userCity, userAddress, userPassword, userRole;
    private Button btnRegister;


    public CrearUsersFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CrearUsersFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CrearUsersFragment newInstance(String param1, String param2) {
        CrearUsersFragment fragment = new CrearUsersFragment();
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
        View v = inflater.inflate(R.layout.fragment_crear_users, container, false);
        // Inicialización de los EditTexts
        userFullName = v.findViewById(R.id.user_full_name);
        userGivenName = v.findViewById(R.id.given_name);
        userEmail = v.findViewById(R.id.email);
        userPhone = v.findViewById(R.id.phone);
        userCity = v.findViewById(R.id.city);
        userAddress = v.findViewById(R.id.Address);
        userPassword = v.findViewById(R.id.password);
        userRole = v.findViewById(R.id.re_password);

        // Inicialización del botón de registro
        btnRegister = v.findViewById(R.id.bt_antialergics);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtén la información del usuario
                String fullName = userFullName.getText().toString();
                String givenName = userGivenName.getText().toString();
                String email = userEmail.getText().toString();
                String phone = userPhone.getText().toString();
                String city = userCity.getText().toString();
                String address = userAddress.getText().toString();
                String password = userPassword.getText().toString();
                String role = userRole.getText().toString();

                // Validación de los datos del usuario
                // Comprobar si el campo de nombre de usuario está vacío
                if (TextUtils.isEmpty(fullName)) {
                    Toast.makeText(getActivity(), "Por favor, ingresa tu nombre de usuario.", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Comprobar si el campo de la contraseña está vacío
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getActivity(), "Por favor, ingresa tu contraseña.", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Comprobar si el campo de correo electrónico está vacío
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getActivity(), "Por favor, ingresa tu correo electrónico.", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Comprobar si el campo de user_role es "patient", "manager" o "doctor"
                if (!userRole.equals("patient") && !userRole.equals("manager") && !userRole.equals("doctor")) {
                    Toast.makeText(getActivity(), "El rol del usuario no es válido. Debe ser 'patient', 'manager' o 'doctor'.", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Si la validación es exitosa, realiza la petición a la API
                registerUser(fullName, givenName, email, phone, city, address, password, role);
            }
        });
        return v;
    }

    private void registerUser(String fullName, String givenName, String email, String phone, String city, String address, String password, String role) {
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        Resources r = getResources();
        String apiUrl = r.getString(R.string.api_base_url);
        String url = apiUrl + "/api/manager_create_account";
        JSONObject jsonBody = new JSONObject();

        try {
            jsonBody.put("user_full_name", fullName);
            jsonBody.put("user_given_name", givenName);
            jsonBody.put("user_email", email);
            jsonBody.put("user_phone", phone);
            jsonBody.put("user_city", city);
            jsonBody.put("user_address", address);
            jsonBody.put("user_password", password);
            jsonBody.put("user_role", role);
            System.out.println("Mensaje a enviar: " + jsonBody);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println("Respuesta recibida: " + response);
                try {
                    String result = response.getString("result");
                    if (result.equals("ok")) {
                        // Caso exitoso, aquí podrías mostrar algún mensaje de éxito
                        Toast.makeText(getActivity(), "Usuario registrado exitosamente!", Toast.LENGTH_SHORT).show();
                    } else {
                        // Caso no exitoso, aquí podrías mostrar algún mensaje de error
                        Toast.makeText(getActivity(), "Error al registrar el usuario. Por favor, intenta nuevamente.", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                // Manejo de error, aquí podrías mostrar algún mensaje de error
                Toast.makeText(getActivity(), "Error al realizar la petición. Por favor, intenta nuevamente.", Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(jsonObjectRequest);
    }
    //El texto insertado se borra al salir del Fragment
    @Override
    public void onStop() {
        super.onStop();

        // Aquí puedes obtener tus EditTexts
        EditText userRoleInput = getView().findViewById(R.id.re_password);
        EditText usernameInput = getView().findViewById(R.id.user_full_name);
        EditText usergivenInput = getView().findViewById(R.id.given_name);
        EditText useradressInput = getView().findViewById(R.id.Address);
        EditText userphoneInput = getView().findViewById(R.id.phone);
        EditText usercityInput = getView().findViewById(R.id.city);
        EditText userpasswordInput = getView().findViewById(R.id.password);
        EditText usermailInput = getView().findViewById(R.id.email);
        // Luego borra el texto en cada uno de ellos
        userRoleInput.setText("");
        usernameInput.setText("");
        usergivenInput.setText("");
        useradressInput.setText("");
        userphoneInput.setText("");
        usercityInput.setText("");
        userpasswordInput.setText("");
        usermailInput.setText("");
    }


}
