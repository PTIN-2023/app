package com.example.appptin;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.appptin.databinding.ActivityMainBinding;
import com.example.appptin.paciente.Patient;
import com.example.appptin.paciente.UserFragment;
import com.example.appptin.paciente.opciones.ConfigPacienteFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity  implements ConfigPacienteFragment.LanguageChangeListener{

    ListView listView;
    String[] components = {"paracetamol", "naproxeno", "dalsi", "diazepam", "dercutane", "pantoprazol", "tiamulina"};
    Patient patient;
    ArrayAdapter<String> arrayAdapter;
    ActivityMainBinding binding;
    private SearchView searchView;

    private EditText searchValue;

    // Variable utilizada para alacenar la lista de medicamentos añadidos para mostrar en la cesta
    private static JSONArray lista_cesta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        listView = findViewById(R.id.listview);

        Intent intent = getIntent();
        String session_token = intent.getStringExtra("session_token");
        SharedPreferences sharedPreferences = getSharedPreferences("UserPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("session_token", session_token);
        getUserInfo(session_token);

        patient = (Patient) getIntent().getSerializableExtra("patient");


        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,components);
        listView.setAdapter(arrayAdapter);

        setDayNight();

        //Al iniciar app volem que obri directament pantalla Home
        replaceFragments(new MedicamentsFragment());
        listView.setVisibility(listView.INVISIBLE);

        lista_cesta = new JSONArray();

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {

            //Switch de les diferents opcions de botó de la Navigation Bar
            switch (item.getItemId()){

                case R.id.home:
                    replaceFragments(new MedicamentsFragment());
                    //Lo primero que aparece son los medicamentos
                    break;
                case R.id.user:
                    //Perfil usuari
                    UserFragment fragment = UserFragment.newInstance(patient);
                    replaceFragments(fragment);

                     //Llamar Activity del médico
                     //Intent intent = new Intent(this, MedicoActivity.class);
                     //startActivity(intent);
                    break;
                case R.id.cistella:
                    replaceFragments(new CistellaFragment());
                    break;
                case R.id.scanQR:
                    replaceFragments(new QrFragment());
                    //Ahora el scan QR se muestra donde antes iba la pantalla opciones
                    break;

            }

            return true;
        });

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.fragment_medicaments, container, false);
        Button searchButton = findViewById(R.id.searchButton);
        searchValue = view.findViewById(R.id.searchView);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String medName = searchValue.getText().toString();
                System.out.println(medName);
            }
        });

        return view;
    }

    private void processSearchText(String searchText) {
        String savedSearchValue = searchText;
        System.out.println(savedSearchValue);
    }

    private void replaceFragments(Fragment fragment){

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout,fragment);
        fragmentTransaction.commit();
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

    public static JSONArray getListaMedicamentos(){
        return lista_cesta;
    }
    public static void setListaMedicamentos(JSONObject objeto){
        lista_cesta.put(objeto);
    }
    // Se llamará desde la ventana CESTA cuando se elimine un medicamento de la lista
    public static void deleteToCart(int indice){
        lista_cesta.remove(indice);
    }
    public static void deleteCesta(){
        for (int i = lista_cesta.length() - 1; i >= 0; i--) {
            deleteToCart(i);
        }
    }

    public static int existeMedicamento(String codi_nacional) throws JSONException {

        int existe = -1;
        for (int i = 0; i < lista_cesta.length(); i++) {
            JSONObject objeto = lista_cesta.getJSONObject(i);
            String nationalCode = objeto.getString("nationalCode");
            if (nationalCode.equals(codi_nacional)) {
                existe = i;
                break;
            }
        }

        return existe;
    }

    public static int getCantidadMedicamento(int i) throws JSONException {
        JSONObject objeto = lista_cesta.getJSONObject(i);
        return objeto.getInt("quantitat");
    }
    public static void setCantidadMedicamento(int i, int elem) throws JSONException {
        if(i > -1){
            JSONObject objeto = lista_cesta.getJSONObject(i);
            int cantidad = objeto.getInt("quantitat");
            cantidad+=elem;
            objeto.put("quantitat", cantidad);
            lista_cesta.put(i,objeto);
        }

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
                                Toast.makeText(MainActivity.this,"Sessió tancada",Toast.LENGTH_SHORT).show();
                                //Borrar SharedPreferences
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.clear();
                                editor.apply(); // O también puedes usar editor.commit();

                                Intent intent = new Intent(MainActivity.this, welcome_page.class);
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