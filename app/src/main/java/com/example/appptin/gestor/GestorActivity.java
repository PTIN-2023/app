package com.example.appptin.gestor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.appptin.R;
import com.example.appptin.gestor.fragments.PeticionesPacientesFragment;
import com.example.appptin.gestor.fragments.inventario.InventarioGestorFragment;
import com.example.appptin.gestor.fragments.inventario.MapaGestor;
import com.example.appptin.gestor.fragments.pefilgestor.PerfilGestorFragment;
import com.example.appptin.gestor.fragments.pefilgestor.opciones.ConfigGestorFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Locale;

public class GestorActivity extends AppCompatActivity  implements ConfigGestorFragment.LanguageChangeListener {

    PerfilGestorFragment gestorFragment;
    InventarioGestorFragment inventarioGestorFragment;
    PeticionesPacientesFragment peticionesPacientesFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestor);

        BottomNavigationView navigation = findViewById(R.id.bottom_navegation_gestor);
        navigation.setOnNavigationItemSelectedListener(nOnNavigationItemSelectedListener);

        //Crear los fragments para el menú
        inventarioGestorFragment = new InventarioGestorFragment();
        gestorFragment = new PerfilGestorFragment();
        peticionesPacientesFragment = new PeticionesPacientesFragment();

        loadFragment(inventarioGestorFragment);

        setDayNight();

    }

    private final BottomNavigationView.OnNavigationItemSelectedListener nOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()){
                case R.id.menu_inventario:
                    loadFragment(inventarioGestorFragment);
                    return true;
                case R.id.menu_perfilGestor:
                    loadFragment(gestorFragment);
                    return true;
                case R.id.menu_opciones:
                    loadFragment(peticionesPacientesFragment);
                    return true;
                case R.id.menu_mapa:
                    Intent intent = new Intent(GestorActivity.this, MapaGestor.class);
                    startActivity(intent);
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