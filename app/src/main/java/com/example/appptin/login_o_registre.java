package com.example.appptin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

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
        // Add animations to the login and registre buttons
        View loginButton = findViewById(R.id.login_button);
        View registreButton = findViewById(R.id.register_button);
        Animation fadeInAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        loginButton.startAnimation(fadeInAnimation);
        registreButton.startAnimation(fadeInAnimation);
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