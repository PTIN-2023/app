package com.example.appptin.gestor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.appptin.R;
import com.example.appptin.gestor.fragments.pefilgestor.PerfilGestorFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class GestorActivity extends AppCompatActivity {

    PerfilGestorFragment gestorFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestor);

        BottomNavigationView navigation = findViewById(R.id.bottom_navegation_gestor);
        navigation.setOnNavigationItemSelectedListener(nOnNavigationItemSelectedListener);

        gestorFragment = new PerfilGestorFragment();

    }

    private final BottomNavigationView.OnNavigationItemSelectedListener nOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()){
                case R.id.menu_perfilGestor:
                    loadFragment(gestorFragment);
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

}