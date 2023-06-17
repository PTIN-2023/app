package com.example.appptin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class login_o_registre extends AppCompatActivity {

    @Override
    public void onBackPressed() {
        // Evitar que se pueda pulsar el botón de retroceso (back)
        // Elimina el siguiente código si deseas habilitar el comportamiento normal del botón de retroceso.
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_oregistre);
    }

    public void login(View view) {
        Intent intent = new Intent(this, login.class);
        startActivity(intent);
    }

    public void registre(View view) {
        Intent intent = new Intent(this, registre.class);
        startActivity(intent);
    }
}