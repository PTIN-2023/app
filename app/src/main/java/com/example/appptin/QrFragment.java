package com.example.appptin;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.PopupWindow;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import com.example.appptin.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class                                              QrFragment extends Fragment {

    String apiUrl;

    Button btnScan;
    EditText txtResultant;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private String resultat;

    private String description;

    private PopupWindow popupWindow;
    private int content;

    private String recepta;

    public QrFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static QrFragment newInstance(String param1, String param2) {
        QrFragment fragment = new QrFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        Resources r = getResources();
        apiUrl = r.getString(R.string.api_base_url);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //((AppCompatActivity)getActivity()).getSupportActionBar().show();
        View rootView = inflater.inflate(R.layout.fragment_qr, container, false);
        TextView txtResultant = rootView.findViewById(R.id.txtResultant);
        Button btnScan = rootView.findViewById(R.id.btnScan);
        Button button_recepta = rootView.findViewById(R.id.button_recepta);

        //BOTO RECEPTA
        button_recepta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obrir pop-up
                popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
            }
        });

        initPopup();

        //return view;
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button btnScan = view.findViewById(R.id.btnScan);
        txtResultant = view.findViewById(R.id.txtResultant);
        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = IntentIntegrator.forSupportFragment(QrFragment.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
                integrator.setPrompt("Lector QR");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(true);
                integrator.setBarcodeImageEnabled(true);
                integrator.initiateScan();
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        RequestQueue queue = Volley.newRequestQueue(getActivity());


        if (result != null) {
            if (result.getContents() == null) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Lectura cancelada")
                        .setMessage("No s'ha llegit cap contingut")
                        .setPositiveButton(android.R.string.ok, null)
                        .show();
            } else {
                String contents = result.getContents();
                if (contents.charAt(0) == '1') {  //Si es un 1 és un paquet
                    String resultant = contents.substring(1);

                    confirmOrder(resultant);
                    /*
                    String url = apiUrl + "/api/check_order";

                    JSONObject jsonBody = new JSONObject();
                    try {
                        content = Integer.parseInt(resultant);
                        jsonBody.put("session_token", login.getSession_token() );
                        jsonBody.put("order_identifier", content);
                        System.out.println("jsonBody " + jsonBody);
                        System.out.println("asdfa"+login.getSession_token());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            System.out.println("a8a: " + response);

                            try {
                                if (response.has("valid")) {
                                    showPopupDialog("Error", response.getString("valid"));
                                } else {
                                    try {
                                        showPopupDialog("Lectura correcta de la comanda n"+ content, response.getString("result"));
                                    } catch (JSONException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();

                            if (error.networkResponse != null && error.networkResponse.statusCode == 500) {
                                // Error interno del servidor (código de respuesta 500)
                                showPopupDialog("Semba que hi ha problemas amb el servidor :/",
                                        "els de la api estan menjant pipas");
                            } else {
                                showPopupDialog("Tenim petits invonvenients per completar la tasca :,|",
                                        "estem treballant per arreglar-ho");
                            }
                        }
                    });
                    queue.add(jsonObjectRequest);*/
                } else if (contents.charAt(0) == '0') {
                    //String recepta = contents.substring(1);
                    //showPopupDialog("Aquest QR correspon a una recepta", "Encara estem treballant per acabar de implementar-ho");

                    String code = contents.substring(1);
                    getRecipe(code);
                    /*
                    System.out.println("Recipe code:" + code);

                    String url = apiUrl + "/api/get_prescription_meds";

                    JSONObject jsonBody = new JSONObject();

                    try {
                        //content = Integer.parseInt(code);
                        System.out.println("Processed order code: " + code);
                        jsonBody.put("session_token", login.getSession_token() );
                        jsonBody.put("prescription_identifier", code);
                        System.out.println("jsonBody " + jsonBody);
                        System.out.println("TOKEN: "+login.getSession_token());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            System.out.println("response from api: " + response);

                            try {
                                if (response.has("valid")) {
                                    showPopupDialog("Error", response.getString("valid"));
                                } else {
                                    try {
                                        showPopupDialog("Lectura correcta de la receta n: "+ code, response.getString("result"));

                                        JSONArray medsDetails = response.getJSONArray("meds_details");

                                        // Per comprovar que recibim els medicaments, a l'hora de provar-ho utilitza un usuari que tingui alguna Recipe
                                        // i al introduir el codi de la Recipe afegeix un 0 al devant.
                                        for (int k = 0; k < medsDetails.length(); k++) {
                                            JSONObject med = medsDetails.getJSONObject(k);
                                            System.out.println("Nom medicina: " + med.getString("med_name"));
                                            System.out.println("Codi nacional: " + med.getString("national_code"));
                                            System.out.println("Tipus d'us: " + med.getString("use_type"));
                                            System.out.println("Tipus d'administració: " + med.getString("type_of_administration"));
                                            System.out.println("PvP: " + med.getString("pvp"));
                                            System.out.println("Forma: " + med.getString("form"));
                                            System.out.println("Excipients: " + med.getString("excipients"));
                                        }

                                    } catch (JSONException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();

                            if (error.networkResponse != null && error.networkResponse.statusCode == 500) {
                                // Error interno del servidor (código de respuesta 500)
                                showPopupDialog("Semba que hi ha problemas amb el servidor :/",
                                        "els de la api estan menjant pipas");
                            } else {
                                showPopupDialog("Tenim petits invonvenients per completar la tasca :,|",
                                        "estem treballant per arreglar-ho");
                            }
                        }
                    });
                    queue.add(jsonObjectRequest);

                */}  else {
                    showPopupDialog("Aquest QR no és correcte", "Prova d'escanejar un QR que et proporcioni Transmed");
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void initPopup() {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.codi_recepta_popup, null);

        RequestQueue queue = Volley.newRequestQueue(getActivity());

        popupWindow = new PopupWindow(
                popupView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true
        );

        EditText editText = popupView.findViewById(R.id.editText);
        Button btnSubmit = popupView.findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = editText.getText().toString();

                if (code.charAt(0) == '0') {
                    // Recipe code
                    code = code.substring(1);
                    getRecipe(code);

                } else if (code.charAt(0) == '1') {
                    // Order code
                    code = code.substring(1);
                    confirmOrder(code);
                } else {
                    showPopupDialog("Codi incorrecte", "Revisa el codi que has introduit per confirmar que és el correcte.");
                }
                // Mostrar el text en la pantalla home

                popupWindow.dismiss();


            }
        });
    }

    private void getRecipe(String prescription_identifier) {
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url = apiUrl + "/api/get_prescription_meds";
        JSONObject jsonBody = new JSONObject();
        try {
            //int content = Integer.parseInt(resultant);
            //Si no va SharedPreferences descomentar
            //jsonBody.put("session_token", login.getSession_token() );
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserPref", Context.MODE_PRIVATE);
            jsonBody.put("session_token", sharedPreferences.getString("session_token", "No value"));
            jsonBody.put("prescription_identifier", prescription_identifier);
            System.out.println("jsonBody " + jsonBody);
            System.out.println("Token: "+login.getSession_token());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("Resposta: " + response);
                        try {
                            resultat = response.getString("result");
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                        if (resultat.equals("ok")) {
                            System.out.println("Tot ha anat bé");

                            JSONArray medsArray = null;  // Assuming the array of medications is under key "meds_details"
                            try {
                                medsArray = response.getJSONArray("meds_details");
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                            for (int i = 0; i < medsArray.length(); i++) {
                                JSONObject medObject = null;
                                JSONObject medicament = new JSONObject();
                                try {
                                    medObject = medsArray.getJSONObject(i);
                                    medicament.put("nationalCode", medObject.getString("national_code"));
                                    medicament.put("medName", medObject.getString("med_name"));
                                    medicament.put("pvp", medObject.getString("pvp"));
                                    medicament.put("quantitat", 1);
                                    //JSONObject medId = medObject("_id"); // Assuming the ID is under key "_id"
                                    MainActivity.setListaMedicamentos(medicament);
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                            showPopupDialog("Els medicaments de la recepta s'han afegit a la cistella.","");
                        } else {
                            System.out.println("Alguna cosa ha fallat");
                            //Fer Pop-Up o algo per notificar l'usuari
                        }
                    }


                }
                        ,new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Error al realizar la solicitud
                        error.printStackTrace();
                    }
                });
        queue.add(jsonObjectRequest);
    }

    /**
     * Funció per enviar a la API que es confirma la recepció d'un paquet amb un codi introduit manualment
     * @param order_identifier identificador de la order/paquet que ha de confirmar
     */
    private void confirmOrder(String order_identifier) {
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        String url = apiUrl + "/api/check_order";

        JSONObject jsonBody = new JSONObject();
        try {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserPref", Context.MODE_PRIVATE);
            jsonBody.put("session_token", sharedPreferences.getString("session_token", "No value"));
            //jsonBody.put("session_token", login.getSession_token() );
            jsonBody.put("order_identifier", order_identifier);
            //System.out.println("jsonBody " + jsonBody);
            //System.out.println("asdfa"+login.getSession_token());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println("a8a: " + response);

                try {
                    if (response.has("valid")) {
                        showPopupDialog("Error", response.getString("valid"));
                    } else {
                        try {
                            // Definitiu:--> showPopupDialog("Lectura correcta del codi del paquet.", "S'ha confirmat l'entrega.");
                            showPopupDialog("Lectura correcta del codi del paquet: "+ order_identifier, response.getString("result")); // Per proves
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();

                if (error.networkResponse != null && error.networkResponse.statusCode == 500) {
                    // Error interno del servidor (código de respuesta 500)
                    showPopupDialog("Semba que hi ha problemas per confirmar el codi",
                            "problema en el servidor remot");
                } else {
                    showPopupDialog("Algo a fallat a l'aplicació.",
                            "Estem treballant per arreglar-ho.");
                }
            }
        });
        queue.add(jsonObjectRequest);
    }
    private void showPopupDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(title)
        .setMessage(message)
        .setPositiveButton("OK", null)
        .show();
    }
}