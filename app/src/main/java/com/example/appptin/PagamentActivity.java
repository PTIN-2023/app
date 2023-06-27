package com.example.appptin;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

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

import com.paypal.checkout.PayPalCheckout;
import com.paypal.checkout.config.CheckoutConfig;
import com.paypal.checkout.config.Environment;
import com.paypal.checkout.config.PaymentButtonIntent;
import com.paypal.checkout.config.SettingsConfig;
import com.paypal.checkout.config.UIConfig;
import com.paypal.checkout.createorder.CreateOrderActions;
import com.paypal.checkout.createorder.CurrencyCode;
import com.paypal.checkout.createorder.OrderIntent;
import com.paypal.checkout.createorder.UserAction;
import com.paypal.checkout.order.Amount;
import com.paypal.checkout.order.AppContext;
import com.paypal.checkout.order.OrderRequest;
import com.paypal.checkout.order.PurchaseUnit;
import com.paypal.checkout.paymentbutton.PaymentButtonContainer;

import java.math.BigDecimal;
import java.util.ArrayList;


public class PagamentActivity extends AppCompatActivity {

    private static final String YOUR_CLIENT_ID = "AdShPAIC7Brc2RE9ATEUGqMHe_c3l4AtscaDpdBBtkYcy1ze7AvOGs9uJ18-SoCOSUF8LbSZmGXTt_X2";
    private LinearLayout cardDetailsLayout;
    PaymentButtonContainer paymentButtonContainer;

    private float preuTotal;
    private TextView preuTotalView;
    private boolean CardLayoutVisible = false;
    private LinearLayout paypalDetailsLayout;
    private boolean paypalLayoutVisible = false;

    //private PayPalConfiguration paypalConfig;
    private Intent paypalIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagament);

        // Initialize PayPal configuration
        PayPalCheckout.setConfig(new CheckoutConfig(
                getApplication(),
                YOUR_CLIENT_ID,
                Environment.SANDBOX,
                CurrencyCode.USD,
                UserAction.PAY_NOW,
                PaymentButtonIntent.CAPTURE,
                new SettingsConfig(true, false),
                new UIConfig(true),
                BuildConfig.APPLICATION_ID + "://paypalpay"
        ));

        preuTotal = getIntent().getFloatExtra("preuTotal", 0.0f);

        preuTotalView = findViewById(R.id.total_price);
        preuTotalView.setText("Total: " + preuTotal + "€");

        System.out.println("PREU TOTAL --> " + preuTotal);

        cardDetailsLayout = findViewById(R.id.card_details_layout);
        paypalDetailsLayout = findViewById(R.id.paypal_details_layout);

        Button btnPaypal = findViewById(R.id.btn_paypal_payment);
        Button btnCard = findViewById(R.id.btn_card_payment);
        Button btnContinue = findViewById(R.id.btn_continue);

        JSONArray lista_cesta = MainActivity.getListaMedicamentos();

        JSONArray medicine_identifiers = new JSONArray();

        // Extraer los nationalCode de cada JSONObject en el JSONArray
        for (int i = 0; i < lista_cesta.length(); i++) {
            JSONObject medicament = null;
            try {
                medicament = lista_cesta.getJSONObject(i);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            String nationalCode = null;
            try {
                nationalCode = medicament.getString("nationalCode");
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            medicine_identifiers.put(nationalCode);
        }

        //Fake JSON amb medicaments per sortir del pas
        /*medicine_identifiers.put("841013");
        medicine_identifiers.put("841009");
        medicine_identifiers.put("753210");*/

        btnCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCardDetails(v);
            }
        });

        btnPaypal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start PayPal service

                paymentButtonContainer.setup(
                        createOrderActions -> {
                            ArrayList<PurchaseUnit> purchaseUnits = new ArrayList<>();
                            purchaseUnits.add(
                                    new PurchaseUnit.Builder()
                                            .amount(
                                                    new Amount.Builder()
                                                            .currencyCode(CurrencyCode.USD)
                                                            .value(String.valueOf(preuTotal))
                                                            .build()
                                            ).build()
                            );
                            OrderRequest order = new OrderRequest(
                                    OrderIntent.CAPTURE,
                                    new AppContext.Builder()
                                            .userAction(UserAction.PAY_NOW)
                                            .build(),
                                    purchaseUnits
                            );
                            createOrderActions.create(order, (CreateOrderActions.OnOrderCreated) null);
                        },
                        approval -> {
                            Log.i(TAG, "OrderId: " + approval.getData().getOrderId());
                        }
                );
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


    public void processCardPayment(View view) {
        // Processament pagament amb targeta
    }
}