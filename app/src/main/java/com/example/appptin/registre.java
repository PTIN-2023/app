package com.example.appptin;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import org.json.JSONException;
import org.json.JSONObject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class registre extends AppCompatActivity {

    EditText input_given_name, input_full_name, input_email, input_password, input_re_password, input_phone, input_city, input_address;

    GoogleSignInClient googleSignInClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registre);

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Inicializar el cliente de inicio de sesión de Google
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);
    }

    public void registre(View view) throws JSONException {
        // get user and password

        input_given_name = findViewById(R.id.given_name);
        input_full_name = findViewById(R.id.user_full_name);
        input_email = findViewById(R.id.email);
        input_phone = findViewById(R.id.phone);
        input_password = findViewById(R.id.password);
        input_re_password = findViewById(R.id.re_password);
        input_city = findViewById(R.id.password);
        input_address = findViewById(R.id.password);

        String given_name = input_given_name.getText().toString();
        String full_name = input_full_name.getText().toString();
        String email = input_email.getText().toString();
        String phone = input_phone.getText().toString();
        String password = input_password.getText().toString();
        String re_password = input_re_password.getText().toString();
        String city = input_city.getText().toString();
        String address = input_address.getText().toString();

        // Aqui hem de fer la consulta amb la API a la base de dades per veure si existeix el usuari


        if (!email.contains("@")) {
            showerror(input_email, "El correu es incorrecte o està buit");
        }
        else if (!password.equals(re_password)){
            showerror(input_password, "Les contrasenyes no coincideixen");
        }
        else {
            RequestQueue queue = Volley.newRequestQueue(this);
            Resources r = getResources();
            String apiUrl = r.getString(R.string.api_base_url);

            System.out.println("Email: " + email);
            System.out.println("Password: " + password);
            System.out.println("Re-Password: " + re_password);

            JSONObject jsonBody = new JSONObject();
            try {
                jsonBody.put("user_full_name", full_name);
                jsonBody.put("user_given_name", given_name);
                jsonBody.put("user_email", email);
                jsonBody.put("user_phone", phone);
                jsonBody.put("user_city", city);
                jsonBody.put("user_address", address);
                jsonBody.put("user_password", address);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String url = r.getString(R.string.api_base_url) + "/api/register"; // Reemplaza con la dirección de tu API
            System.out.println(url);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                String result = response.getString("result");
                                System.out.println(result);
                                if (result.equals("ok")) {
                                    // Registro exitoso, navegar a la siguiente actividad (por ejemplo, Welcome_popup)
                                    Intent intent = new Intent(getApplication(), MainActivity.class);
                                    startActivity(intent);
                                } else {
                                    // Error en el registro
                                    // Muestra un mensaje de error al usuario
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

    public void showerror(EditText input, String s){
        input.setError(s);
        input.requestFocus();
    }

    public void registre_google(View view) {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private static final int RC_SIGN_IN = 9001;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Resultado del inicio de sesión de Google
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Inicio de sesión exitoso
                GoogleSignInAccount account = task.getResult(ApiException.class);

                // Aquí se puede obtener el nombre, correo electrónico y otros datos del usuario de la cuenta de Google
                // y utilizarlos para el inicio de sesión en la aplicación
                SharedPreferences preferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("email", account.getEmail());
                editor.putString("name", account.getDisplayName());
                editor.apply();

                // Guardamos en variables el correo y el password para acceder a la base de datos
                String email = preferences.getString("email", "");



                // Cambiamos de actividad
                // Aquí se puede obtener el nombre, correo electrónico y otros datos del usuario de la cuenta de Google
                // y utilizarlos para el inicio de sesión en la aplicación
                Intent intent = new Intent(this, Welcome_popup.class); // Reemplaza NuevaActividad con el nombre de la actividad a la que quieres ir
                startActivity(intent);

            } catch (ApiException e) {
                // Inicio de sesión fallido
                Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            }
        }
    }
}