package com.example.appptin.medico;
import com.example.appptin.R;
import com.example.appptin.medico.fragments.historialPeticion.HistorialPeticionFragment;
import com.example.appptin.medico.fragments.perfilmedico.PerfilMedicoFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import java.io.IOException;

public class MedicoActivity extends AppCompatActivity {

    //AprobarFragment aprobarFragment = new AprobarFragment();
    HistorialPeticionFragment aprobarFragment;
    HistorialPeticionFragment pacienteFragment;
    HistorialPeticionFragment peticionFragment;
    PerfilMedicoFragment medicoFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medico);

        BottomNavigationView navigation = findViewById(R.id.bottom_navegation_medico);
        navigation.setOnNavigationItemSelectedListener(nOnNavigationItemSelectedListener);


        try {
            aprobarFragment = new HistorialPeticionFragment("Peticions per aprovar",1,0,false,this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            peticionFragment = new HistorialPeticionFragment("Historial Peticions",2,0,false,this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            pacienteFragment = new HistorialPeticionFragment("Historial Pacients",3,0,false,this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

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
                    loadFragment(pacienteFragment);
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
}