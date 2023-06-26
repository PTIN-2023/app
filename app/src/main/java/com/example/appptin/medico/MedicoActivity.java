package com.example.appptin.medico;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.appptin.R;
import com.example.appptin.login;
import com.example.appptin.login_o_registre;
import com.example.appptin.medico.fragments.recetaPaciente.RecetaFragment;
import com.example.appptin.medico.fragments.historialPeticion.HistorialPeticionFragment;
import com.example.appptin.medico.fragments.perfilmedico.PerfilMedicoFragment;
import com.example.appptin.medico.fragments.perfilmedico.opciones.ConfigMedicoFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class MedicoActivity extends AppCompatActivity implements ConfigMedicoFragment.LanguageChangeListener{

    //AprobarFragment aprobarFragment = new AprobarFragment();
    HistorialPeticionFragment aprobarFragment;
    HistorialPeticionFragment peticionFragment;
    RecetaFragment historialPacienteFragment;
    PerfilMedicoFragment medicoFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medico);

        BottomNavigationView navigation = findViewById(R.id.bottom_navegation_medico);
        navigation.setOnNavigationItemSelectedListener(nOnNavigationItemSelectedListener);

        Intent intent = getIntent();
        String session_token = intent.getStringExtra("session_token");
        SharedPreferences sharedPreferences = this.getSharedPreferences("UserPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("session_token", session_token);
        editor.apply();
        getUserInfo(session_token);

        setDayNight();

        try {
            aprobarFragment = new HistorialPeticionFragment("Peticions per aprovar",1,0,this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            peticionFragment = new HistorialPeticionFragment("Historial Peticions",2,0,this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        historialPacienteFragment = new RecetaFragment();

        medicoFragment = new PerfilMedicoFragment();

        loadFragment(aprobarFragment);
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener nOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()){
                case R.id.menu_aprobarPeticion:
                    loadFragment(aprobarFragment);
                    return true;
                case R.id.menu_historialPeticion:
                    loadFragment(peticionFragment);
                    return true;
                case R.id.menu_historialPaciente:
                    loadFragment(historialPacienteFragment);
                    return true;
                case R.id.menu_perfilMedico:
                    loadFragment(medicoFragment);
                    return true;
            }
            return false;
        }
    };

    public void loadFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.commit();

    }

    /*
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Confirmació de logout")
                .setMessage("Estàs segur que vols fer logout?")
                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Esborra les dades d'inici de sessió i redirigeix a l'activitat de login
                        SharedPreferences sharedPreferences = getSharedPreferences("UserPref", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.clear();
                        editor.apply();
                        startActivity(new Intent(MedicoActivity.this, login_o_registre.class));
                        finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
*/
    //Función para activar modo oscuro
    public void setDayNight(){
        SharedPreferences sp = getSharedPreferences("SP", this.MODE_PRIVATE);
        int theme = sp.getInt("Theme", 1);
        if(theme==0){
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        else{
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    public void onLanguageChanged() {
        finish();
        startActivity(getIntent());
    }

    private void getUserInfo(String session_token) {
        RequestQueue queue = Volley.newRequestQueue(this);
        Resources r = getResources();
        String apiUrl = r.getString(R.string.api_base_url);
        String url = apiUrl + "/api/user_info";
        System.out.println(url);
        System.out.println("GetUserInfo ST: " + session_token);
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("token", session_token);
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
                            String result = response.getString("result");

                            // Utiliza los valores extraídos según sea necesario
                            if (result.equals("ok")) {
                                System.out.println("L'usuari existeix");
                                // Obtiene las SharedPreferences
                                SharedPreferences sharedPreferences = getSharedPreferences("UserPref", Context.MODE_PRIVATE);

                                // Edita las SharedPreferences
                                SharedPreferences.Editor editor = sharedPreferences.edit();

                                editor.putString("user_full_name", response.getString("user_full_name"));
                                editor.putString("user_given_name", response.getString("user_given_name"));
                                editor.putString("user_email", response.getString("user_email"));
                                editor.putString("user_phone", response.getString("user_phone"));
                                editor.putString("user_city", response.getString("user_city"));
                                editor.putString("user_address", response.getString("user_address"));
                                editor.putString("user_picture", response.getString("user_picture"));

                                // Aplica los cambios
                                System.out.println("EL TOKEN ES --> " + session_token);
                                editor.apply();

                            }
                            else {
                                System.out.println("Error");
                            }
                        }
                        catch (JSONException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Error al realizar la solicitud
                        error.printStackTrace();
                        Log.w("Error login", "ATENCION: Ha habido un error.");
                    }
                });
        queue.add(jsonObjectRequest);
    }

}