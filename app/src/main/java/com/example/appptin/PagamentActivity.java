package com.example.appptin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.appptin.paciente.Patient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
        Button btnContinue = findViewById(R.id.btn_continue);


        //Fake JSON amb medicaments per sortir del pas
        JSONArray medicine_identifiers = new JSONArray();
        medicine_identifiers.put("841013");
        medicine_identifiers.put("841009");
        medicine_identifiers.put("753210");

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

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Hara falta obtener el metodo de pago
                JSONArray lista_cesta = MainActivity.getListaMedicamentos();

                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                Resources r = getResources();
                String apiUrl = r.getString(R.string.api_base_url);
                String url = apiUrl + "/api/make_order";
                System.out.println(url);
                SharedPreferences sharedPreferences = getSharedPreferences("UserPref", Context.MODE_PRIVATE);

                String session_token = sharedPreferences.getString("session_token", "Valor vacio");

                JSONObject jsonBody = new JSONObject();
                try {
                    System.out.println(medicine_identifiers);
                    System.out.println(session_token);
                    jsonBody.put("session_token", session_token);
                    jsonBody.put("medicine_identifiers", medicine_identifiers); //Canviar per medicaments de la cistella
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                        (Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    //boolean exists = response.getBoolean("exists");
                                    System.out.println("MENSAJE: " + response);
                                    String result = response.getString("result");

                                    // Utiliza los valores extraídos según sea necesario
                                    if (result.equals("ok")) {
                                        System.out.println("COmanda realitzada");
                                        // Obtiene las SharedPreferences
                                    }
                                    else {
                                        System.out.println("Error");
                                    }
                                }
                                catch (JSONException ex) {
                                    throw new RuntimeException(ex);
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // Error al realizar la solicitud
                                error.printStackTrace();
                                Log.w("Error login", "ATENCION: Ha habido un error, el pedido no se ha realizado.");
                            }
                        });
                queue.add(jsonObjectRequest);
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