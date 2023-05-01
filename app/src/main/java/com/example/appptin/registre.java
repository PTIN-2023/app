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

import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import com.example.appptin.login;

public class registre extends AppCompatActivity {

    EditText inputcorreu, input_contrassenya, input_repetir_contrassenya, input_usuari, input_telefon;

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

    public void registre(View view) {
        // get user and password

        inputcorreu = (EditText) findViewById(R.id.correu);
        input_usuari = (EditText) findViewById(R.id.usuari);
        input_telefon = (EditText) findViewById(R.id.telefon);
        input_contrassenya = (EditText) findViewById(R.id.contrassenya);
        input_repetir_contrassenya = (EditText) findViewById(R.id.repetir_contrassenya);
        String _usuari = (input_usuari.getText()).toString();
        String _correu = (inputcorreu.getText()).toString();
        String _telefon = (input_telefon.getText()).toString();
        String _contrassenya = (input_contrassenya.getText()).toString();
        String _repetir_contrassenya = (input_repetir_contrassenya.getText()).toString();

        // Aqui fem la consulta amb la API a la base de dades per veure si existeix el usuari
        if(!_correu.contains("@")){
            showerror(inputcorreu, "El correu es incorrecte o està buit");
        }
        else{
            Intent intent = new Intent(this, Welcome_popup.class);
            startActivity(intent);
        }

        if (!_correu.contains("@")) {
            showerror(inputcorreu, "El correu es incorrecte o està buit");
        } else {
            JSONObject jsonBody = new JSONObject();
            try {
                jsonBody.put("name", _usuari);
                jsonBody.put("email", _correu);
                jsonBody.put("role", "pacient");
                jsonBody.put("phone", _telefon); // Añade el número de teléfono del usuario aquí
                jsonBody.put("password", _contrassenya);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            RequestQueue queue = Volley.newRequestQueue(this);
            Resources r = getResources();
            String apiUrl = r.getString(R.string.api_base_url);
            String url = apiUrl + "/api/register"; // Reemplaza con la dirección de tu API
            System.out.println(url);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                String result = response.getString("result");

                                if (result.equals("ok")) {
                                    // Registro exitoso, navegar a la siguiente actividad (por ejemplo, Welcome_popup)
                                    Intent intent = new Intent(getApplication(), login.class);
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