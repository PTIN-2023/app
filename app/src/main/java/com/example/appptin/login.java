package com.example.appptin;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.appptin.gestor.GestorActivity;
import com.example.appptin.medico.MedicoActivity;
import com.example.appptin.paciente.Patient;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;



public class login extends AppCompatActivity {
    EditText inputcorreu, input_contrassenya;

    // Declarar variables globales
    GoogleSignInClient googleSignInClient;
    private String oauthToken;

    private PopupWindow popupWindow;

    public static String getSession_token() {
        return session_token;
    }

    private static String session_token;

    private ProgressDialog progressDialog;
    private boolean progressDialogShowing = false;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void login(View view) throws JSONException {

        // get user and password
        inputcorreu = (EditText) findViewById(R.id.email);
        input_contrassenya = (EditText) findViewById(R.id.password);
        String _correu = (inputcorreu.getText()).toString();
        String _contrassenya = (input_contrassenya.getText()).toString();

        // Encriptar contrassenya (comentat fins que els de la api implementing els tokens)
        /*
        Map<String, Object> message = new HashMap<>();
        message.put("password",_contrassenya);
        message.put("exp", new Date(System.currentTimeMillis()+300000));
        message.put("iss","appA4");

        String secret = "jwt1234-piramide-quadrada-gas-salvatge-iceberg-piña-Meren";

        String token = Jwts.builder()
                .setClaims(message)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
        */
        // Aqui fem la consulta amb la API a la base de dades per veure si existeix el usuari

        //!_correu.contains("@")
        if(_correu.isEmpty() && _contrassenya.isEmpty()){
            showerror(inputcorreu, "El correu esta buit");
            showerror(input_contrassenya, "La contrassenya està buida");
        }

        else if(_contrassenya.isEmpty()){
            showerror(input_contrassenya, "La contrassenya està buida");
        }

        else if(_correu.isEmpty()){
            showerror(inputcorreu, "El correu esta buit");
        }

        else if(!_correu.contains("@")){
            showerror(inputcorreu, "El correu no conté '@'");
        }

        else {
            //Toast.makeText(this, "Call Registration Method", Toast.LENGTH_SHORT).show();
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Logging in...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            login(_correu, _contrassenya);

        }
    }

    public void showerror(EditText input, String s){
        input.setError(s);
        input.requestFocus();
    }

    private static final int RC_SIGN_IN = 9001;


    private boolean login(String email, String password) throws JSONException {

        boolean exists = false;
        RequestQueue queue = Volley.newRequestQueue(this);
        Resources r = getResources();
        String apiUrl = r.getString(R.string.api_base_url);
        String url = apiUrl + "/api/login";
        System.out.println(url);
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("user_email", email);
            jsonBody.put("user_password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //boolean exists = response.getBoolean("exists");
                            System.out.println("MENSAJE: " + response);
                            String given_name = response.getString("user_given_name");
                            //String email = response.getString("user_email");
                            String password = response.isNull("password") ? null : response.getString("password");
                            String result = response.getString("result");
                            String role = response.getString("user_role");
                            String session_token = response.getString("user_token");

                            // Utiliza los valores extraídos según sea necesario
                            if (result.equals("ok")) {
                                System.out.println("L'usuari existeix");
                                Toast.makeText(login.this,"Welcome " + given_name + "!",Toast.LENGTH_SHORT).show();
                                if (role.equals("patient")){

                                    navigateToMainActivity(session_token);

                                }
                                else if (role.equals("doctor")){
                                    System.out.println("Login token: " + session_token);
                                    navigateToMetgeActivity(session_token);
                                }
                                else if (role.equals("manager")){
                                    navigateToGestorActivity(session_token);
                                }

                            } else {
                                System.out.println("L'usuari NO existeix");
                                //Fer Pop-Up o algo per notificar l'usuari
                                showerror("L'usuari NO existeix. Crea un compte");
                            }
                        } catch (JSONException e) {
                            //showAlert("user/password not found");
                            showerror("L'usuari NO existeix. Crea un compte");
                            //e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Error al realizar la solicitud
                        error.printStackTrace();
                        Log.w("Error login", "ATENCION: Ha habido un error, se procedera a cargar una sesion de prueba");
                        //Patient patient = new Patient(
                        //        "45hgghhbhkkK9*^¨cDDG",
                        //        "Manolo de los Palotes",
                        //        "Manolin",
                        //        "manolo@gmail.com",
                        //        "608745633",
                        //        "Villalgordo",
                        //        "Calle para siempre 6",
                        //        null);
                        //navigateToMainActivity("45hgghhbhkkK9*^¨cDDG");
                    }
                });
        queue.add(jsonObjectRequest);

        // Dismiss the progress dialog after 2 seconds
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dismissProgressDialog();
            }
        }, 2000);

        return exists;
    }

    private void dismissProgressDialog() {
        try {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        } catch (IllegalArgumentException e) {
            // Catch the exception to handle the case when the activity is already destroyed or not attached to the window
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        dismissProgressDialog();
    }

    private void showerror(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Error");
        builder.setMessage(message);
        builder.setPositiveButton("OK", null);
        builder.show();
    }

    private void navigateToMainActivity(String session_token) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("session_token", session_token);
        System.out.println("AAA "+ session_token);
        startActivity(intent);
        finish(); // Esto cerrará la actividad actual (LoginActivity, por ejemplo)
    }

    private void navigateToMetgeActivity(String session_token) {
        Intent intent = new Intent(this, MedicoActivity.class);
        intent.putExtra("session_token", session_token);
        startActivity(intent);
        finish(); // Esto cerrará la actividad actual (LoginActivity, por ejemplo)
    }

    private void navigateToGestorActivity(String session_token) {
        Intent intent = new Intent(this, GestorActivity.class);
        intent.putExtra("session_token", session_token);
        startActivity(intent);
        finish(); // Esto cerrará la actividad actual (LoginActivity, por ejemplo)
    }

    private void showAlert(String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("ERROR")
                .setMessage(message)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

}