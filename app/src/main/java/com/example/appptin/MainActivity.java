package com.example.appptin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.widget.SearchView;
import com.example.appptin.databinding.ActivityMainBinding;
import com.example.appptin.medico.MedicoActivity;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    String[] components = {"paracetamol", "naproxeno", "dalsi", "diazepam", "dercutane", "pantoprazol", "tiamulina"};

    ArrayAdapter<String> arrayAdapter;
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        listView = findViewById(R.id.listview);

        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,components);
        listView.setAdapter(arrayAdapter);


        //Al iniciar app volem que obri directament pantalla Home
        replaceFragments(new HomeFragment());
        listView.setVisibility(listView.INVISIBLE);

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {

            //Switch de les diferents opcions de botó de la Navigation Bar
            switch (item.getItemId()){

                case R.id.home:
                    replaceFragments(new HomeFragment());
                    break;
                case R.id.user:
                    //Perfil usuari
                    //replaceFragments(new UserFragment());

                    //Llamar Activity del médico
                     Intent intent = new Intent(this, MedicoActivity.class);
                     startActivity(intent);
                    break;
                case R.id.cistella:
                    replaceFragments(new CistellaFragment());
                    break;
                case R.id.opcions:
                    replaceFragments(new OpcionsFragment());
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

    @Override
    //Funció de barra de cerca
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.search_bar,menu);

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
    }

    private void getMedicaments(String text){


    }
}