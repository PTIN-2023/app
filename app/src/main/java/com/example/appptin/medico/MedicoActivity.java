package com.example.appptin.medico;
import com.example.appptin.R;
import com.example.appptin.medico.fragments.perfilmedico.PerfilMedicoFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

public class MedicoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medico);

        BottomNavigationView navigation = findViewById(R.id.bottom_navegation_medico);

        navigation.setOnNavigationItemSelectedListener(nOnNavigationItemSelectedListener);

        //aprobarFragment = new HistorialPeticionFragment("Peticions per aprovar",3,0,false);
        //loadFragment(aprobarFragment);
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener nOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()){
                case R.id.menu_aprobarPeticion:
                    //loadFragment(aprobarFragment);
                    return true;
                case R.id.menu_historialPeticion:
                    //loadFragment(peticionFragment);
                    return true;
                case R.id.menu_historialPaciente:
                    //loadFragment(pacienteFragment);
                    return true;
                case R.id.menu_perfilMedico:
                    loadFragment(new PerfilMedicoFragment());
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