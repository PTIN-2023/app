package com.example.appptin;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

public class welcome_page extends AppCompatActivity {

    ImageView imageView;
    ProgressBar pb;
    int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);
        imageView = (ImageView)findViewById(R.id.imageView);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.welcome_animation);
        imageView.setAnimation(animation);

        animation.setAnimationListener(new Animation.AnimationListener(){
            @Override
            public void onAnimationStart(Animation animation){

            }

            @Override
            public void onAnimationEnd(Animation animation){
                finish();
                startActivity(new Intent(getApplicationContext(), login_o_registre.class));
            }

            @Override
            public void onAnimationRepeat(Animation animation){

            }
        });

        prog();
    }

    public void prog(){

        pb = (ProgressBar)findViewById(R.id.progressBar);

        final Timer t = new Timer();
        TimerTask tt = new TimerTask(){
            @Override
            public void run(){
                counter++;
                pb.setProgress(counter);

                // cada vez que se incrementa el valor, se incrementa un 1% de la barra
                if(counter == 100)
                    t.cancel();
            }
        };
        // el 50 indica que en 4 segundos se llena la barra
        t.schedule(tt, 0, 40);
    }
}