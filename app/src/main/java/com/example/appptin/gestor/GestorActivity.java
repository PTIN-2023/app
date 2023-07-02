package com.example.appptin.gestor;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.appptin.R;
import com.example.appptin.gestor.fragments.CrearUsersFragment;
import com.example.appptin.gestor.fragments.MapaFragment;
import com.example.appptin.gestor.fragments.flota.FlotaFragment;
import com.example.appptin.gestor.fragments.inventario.InventarioGestorFragment;
import com.example.appptin.gestor.fragments.inventario.MapaGestor;
import com.example.appptin.gestor.fragments.pefilgestor.PerfilGestorFragment;
import com.example.appptin.gestor.fragments.pefilgestor.opciones.ConfigGestorFragment;
import com.example.appptin.gestor.fragments.pefilgestor.opciones.EstatPeticionsGestorFragment;
import com.example.appptin.medico.MedicoActivity;
import com.example.appptin.gestor.fragments.MapaFragment;
import com.example.appptin.welcome_page;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

public class GestorActivity extends AppCompatActivity  implements ConfigGestorFragment.LanguageChangeListener {

    PerfilGestorFragment gestorFragment;
    InventarioGestorFragment inventarioGestorFragment;
    //CrearUsersFragment crearUsersFragment;
    FlotaFragment flotaFragment;
    EstatPeticionsGestorFragment estatPeticionsGestorFragment;

    MapaFragment mapaFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestor);

        BottomNavigationView navigation = findViewById(R.id.bottom_navegation_gestor);
        navigation.setOnNavigationItemSelectedListener(nOnNavigationItemSelectedListener);

        Intent intent = getIntent();
        String session_token = intent.getStringExtra("session_token");
        SharedPreferences sharedPreferences = getSharedPreferences("UserPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("session_token", session_token);
        getUserInfo(session_token);

        //Crear los fragments para el menú
        inventarioGestorFragment = new InventarioGestorFragment();
        gestorFragment = new PerfilGestorFragment();
        estatPeticionsGestorFragment = new EstatPeticionsGestorFragment();
        //crearUsersFragment = new CrearUsersFragment();
        flotaFragment = new FlotaFragment();
        mapaFragment = new MapaFragment();

        loadFragment(inventarioGestorFragment);



        setDayNight();

    }

    private final BottomNavigationView.OnNavigationItemSelectedListener nOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()){
                case R.id.menu_gestionarUsuarios:
                    //loadFragment(crearUsersFragment);
                    loadFragment(flotaFragment);
                    return true;
                case R.id.menu_inventario:
                    loadFragment(inventarioGestorFragment);
                    return true;
                case R.id.menu_perfilGestor:
                    loadFragment(gestorFragment);
                    return true;
                case R.id.menu_opciones:
                    loadFragment(estatPeticionsGestorFragment);
                    return true;
                case R.id.menu_mapa:
                    loadFragment(mapaFragment);
                    return true;
                    /*Intent intent = new Intent(GestorActivity.this, MapaGestor.class);
                    startActivity(intent);
                    return true;*/
            }
            return false;
        }
    };

    public void loadFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.commit();

    }

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
        System.out.println(session_token);
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("session_token", session_token);
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


                                editor.putString("session_token", session_token);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Cerrar Sesión")
                .setMessage("Seguro que quieres salir de tu sesión?")
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        logout();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private boolean logout() {
        boolean exists = false;
        RequestQueue queue = Volley.newRequestQueue(this);
        Resources r = getResources();
        String apiUrl = r.getString(R.string.api_base_url);
        String url = apiUrl + "/api/logout"; // Reemplaza con la dirección de tu API
        System.out.println(url);
        JSONObject jsonBody = new JSONObject();
        SharedPreferences sharedPreferences = getSharedPreferences("UserPref", Context.MODE_PRIVATE);
        try {
            System.out.println("Token logout: " + sharedPreferences.getString("session_token", "No value"));
            jsonBody.put("session_token", sharedPreferences.getString("session_token", "No value"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            System.out.println("MENSAJE: " + response);
                            String result = response.getString("result");

                            // Utiliza los valores extraídos según sea necesario
                            if (result.equals("ok")) {
                                System.out.println("S'ha tancat la sessió");
                                Toast.makeText(GestorActivity.this,"Sessió tancada",Toast.LENGTH_SHORT).show();
                                //Borrar SharedPreferences
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.clear();
                                editor.apply(); // O también puedes usar editor.commit();

                                Intent intent = new Intent(GestorActivity.this, welcome_page.class);
                                startActivity(intent);

                            } else {
                                System.out.println("Token de sesió incorrecte");
                                //Fer Pop-Up o algo per notificar l'usuari
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

        queue.add(jsonObjectRequest);
        return exists;
    }

}