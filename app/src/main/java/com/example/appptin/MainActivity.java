package com.example.appptin;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.appptin.databinding.ActivityMainBinding;
import com.example.appptin.paciente.Patient;
import com.example.appptin.paciente.UserFragment;
import com.example.appptin.paciente.opciones.ConfigPacienteFragment;

public class MainActivity extends AppCompatActivity  implements ConfigPacienteFragment.LanguageChangeListener{

    ListView listView;
    String[] components = {"paracetamol", "naproxeno", "dalsi", "diazepam", "dercutane", "pantoprazol", "tiamulina"};
    Patient patient;
    ArrayAdapter<String> arrayAdapter;
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        listView = findViewById(R.id.listview);

        patient = (Patient) getIntent().getSerializableExtra("patient");

        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,components);
        listView.setAdapter(arrayAdapter);

        setDayNight();

        //Al iniciar app volem que obri directament pantalla Home
        replaceFragments(new MedicamentsFragment());
        listView.setVisibility(listView.INVISIBLE);

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
                    replaceFragments(new HomeFragment());
                    //Ahora el scan QR se muestra donde antes iba la pantalla opciones
                    break;

            }

            return true;
        });

    }

    private void replaceFragments(Fragment fragment){

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout,fragment);
        fragmentTransaction.commit();
    }

    //@Override
    //Funció de barra de cerca
    /*public boolean onCreateOptionsMenu(Menu menu) {

        //getMenuInflater().inflate(R.menu.search_bar,menu);

        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Prem aquí per a cercar");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                System.out.println("Test");
                getMedicaments(query);
                MedicamentsFragment MedicamentsFragment = new MedicamentsFragment();
                // Reemplaza el fragmento actual con el MedicamentsFragment
                replaceFragments(MedicamentsFragment);
                // Tanquem el teclat virtual
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                arrayAdapter.getFilter().filter(newText);
                searchView.setVisibility(listView.VISIBLE);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }*/

    private void getMedicaments(String text){


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

}