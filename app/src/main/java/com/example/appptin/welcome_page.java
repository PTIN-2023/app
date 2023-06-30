package com.example.appptin;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;

import java.util.Timer;
import java.util.TimerTask;

public class welcome_page extends AppCompatActivity {

    ImageView logo, splashimg;
    LottieAnimationView lottieAnimationView;
    ProgressBar pb;
    int counter = 0;
    /*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);
        getWindow().getDecorView().setBackgroundColor(Color.BLACK); // Set background color

        imageView = findViewById(R.id.imageView);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.welcome_animation);
        imageView.setAnimation(animation);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                // Animation start
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                finish();
                startActivity(new Intent(getApplicationContext(), login_o_registre.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left); // Apply slide animation
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // Animation repeat
            }
        });

        prog();
    }

    public void prog() {
        pb = findViewById(R.id.progressBar);
        pb.getProgressDrawable().setColorFilter(Color.rgb(255, 165, 0), android.graphics.PorterDuff.Mode.SRC_IN); // Set progress bar color

        final Timer t = new Timer();
        TimerTask tt = new TimerTask() {
            @Override
            public void run() {
                counter++;
                pb.setProgress(counter);

                // Each time the value is incremented, the progress bar fills by 1%
                if (counter == 100)
                    t.cancel();
            }
        };
        // The interval of 40 milliseconds indicates that the progress bar will be filled in 4 seconds
        t.schedule(tt, 0, 40);
    }
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);

        logo = findViewById(R.id.logo);
        splashimg = findViewById(R.id.bg);
        lottieAnimationView = findViewById(R.id.lottie);

        splashimg.animate().translationY(-2500).setDuration(1000).setStartDelay(4000);
        logo.animate().translationY(2300).setDuration(1000).setStartDelay(4000);
        lottieAnimationView.animate().translationY(2000).setDuration(1000).setStartDelay(4000);

        // Start the login_o_registre activity after the animation finishes
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        startActivity(new Intent(getApplicationContext(), login_o_registre.class));
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        finish();
                    }
                },
                5000 // Delay for 5 seconds (1000 milliseconds = 1 second)
        );

    }
}
