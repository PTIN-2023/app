package com.example.appptin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class PagamentActivity extends AppCompatActivity {

    private LinearLayout cardDetailsLayout;
    private boolean CardLayoutVisible = false;
    private LinearLayout paypalDetailsLayout;
    private boolean paypalLayoutVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagament);

        cardDetailsLayout = findViewById(R.id.card_details_layout);
        paypalDetailsLayout = findViewById(R.id.paypal_details_layout);

        Button btnPaypal = findViewById(R.id.btn_paypal_payment);
        Button btnCard = findViewById(R.id.btn_card_payment);

        btnCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCardDetails(v);
            }
        });

        btnPaypal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPayPalDetails(v);
            }
        });
    }

    public void showCardDetails(View view) {

        if (CardLayoutVisible) {
            cardDetailsLayout.setVisibility(View.GONE);
            CardLayoutVisible = false;
        } else {
            cardDetailsLayout.setVisibility(View.VISIBLE);
            CardLayoutVisible = true;
        }
    }

    public void showPayPalDetails(View view) {

        if (paypalLayoutVisible) {
            paypalDetailsLayout.setVisibility(View.GONE);
            paypalLayoutVisible = false;
        } else {
            paypalDetailsLayout.setVisibility(View.VISIBLE);
            paypalLayoutVisible = true;
        }
    }

    public void processCardPayment(View view) {
        // Processament pagament amb targeta
    }
}