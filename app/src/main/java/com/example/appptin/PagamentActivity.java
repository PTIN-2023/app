package com.example.appptin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
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
import android.widget.Toast;

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

import com.example.appptin.paciente.opciones.DatosPacienteFragment;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import java.math.BigDecimal;
import com.example.appptin.R;


public class PagamentActivity extends AppCompatActivity {

    private LinearLayout cardDetailsLayout;

    private float preuTotal;
    private TextView preuTotalView;
    private boolean CardLayoutVisible = false;
    private LinearLayout paypalDetailsLayout;
    private boolean paypalLayoutVisible = false;

    private PayPalConfiguration paypalConfig;
    private Intent paypalIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagament);

        // Initialize PayPal configuration
        paypalConfig = new PayPalConfiguration()
                .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX) // Use ENVIRONMENT_PRODUCTION for production
                .clientId("AdShPAIC7Brc2RE9ATEUGqMHe_c3l4AtscaDpdBBtkYcy1ze7AvOGs9uJ18-SoCOSUF8LbSZmGXTt_X2");


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
        System.out.println("listacesta" +lista_cesta);
        System.out.println(lista_cesta);
        JSONArray medicine_identifiers = new JSONArray();
        System.out.println("medident" + medicine_identifiers);
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
            int quantity = 0;
            try {
                quantity = medicament.getInt("quantitat");
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            JSONArray identifier = new JSONArray();
            identifier.put(nationalCode);
            identifier.put(quantity);

            medicine_identifiers.put(identifier);
        }
        System.out.println("meds"+medicine_identifiers);
        //for (int i = 0; i < lista_cesta.length(); i++) {
        //    JSONObject medicament = null;
//
        //    //try {
        //    //    medicament = lista_cesta.getJSONObject(i);
        //    //} catch (JSONException e) {
        //    //    throw new RuntimeException(e);
        //    //}
        //    //String nationalCode = null;
        //    //try {
        //    //    nationalCode = medicament.getString("nationalCode");
        //    //} catch (JSONException e) {
        //    //    throw new RuntimeException(e);
        //    //}
        //    //medicine_identifiers.put(nationalCode);
        //}


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
                paypalIntent = new Intent(PagamentActivity.this, PayPalService.class);
                paypalIntent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, paypalConfig);
                startService(paypalIntent);

                // Show PayPal details or start payment flow
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
                                        System.out.println("Comanda realitzada");
                                        // Obtiene las SharedPreferences
                                        Toast.makeText(getBaseContext(), "Comanda realitzada", Toast.LENGTH_SHORT).show();
                                        volver_cesta();

                                    }
                                    else {
                                        System.out.println("Error");
                                        Toast.makeText(getBaseContext(), "No realizat comanda", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                catch (JSONException ex) {
                                    Toast.makeText(getBaseContext(), "Error en la respota", Toast.LENGTH_SHORT).show();
                                    throw new RuntimeException(ex);
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // Error al realizar la solicitud
                                Toast.makeText(getBaseContext(), "Error servidor", Toast.LENGTH_SHORT).show();
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

        PayPalPayment payment = new PayPalPayment(new BigDecimal(preuTotal), "USD", "Payment Description",
                PayPalPayment.PAYMENT_INTENT_SALE);

        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, paypalConfig);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirm != null) {
                    Log.d("PAYPAL", confirm.toJSONObject().toString());
                    // Payment completed. Process the result as needed.
                    // You can access the payment details from confirm.toJSONObject().
                    // Make necessary API calls or update UI accordingly.
                }
            } else if (resultCode == RESULT_CANCELED) {
                Log.d("PAYPAL", "Payment canceled by user.");
                // Handle payment cancellation as needed.
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.e("PAYPAL", "Invalid PayPal payment or configuration.");
                // Handle invalid payment or configuration as needed.
            }
        }
    }

    public void volver_cesta(){

        // Eliminar todos los elementos de la cesta
        MainActivity.deleteCesta();


        FragmentManager fragmentManager = this.getSupportFragmentManager(); // Si estás en un Fragment, utiliza getFragmentManager()
        if (fragmentManager.getBackStackEntryCount() > 0) {
            // Retrocede en la pila de fragmentos
            fragmentManager.popBackStack();
        }
        //FragmentManager fragmentManager = this.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        //Cambio de Fragment - CISTELLA
        CistellaFragment cistellaFragment = new CistellaFragment();
        FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout, cistellaFragment);
        transaction.addToBackStack(null);
        transaction.commit();




    }



    public void processCardPayment(View view) {
        // Processament pagament amb targeta
    }



}