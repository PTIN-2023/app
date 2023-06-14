package com.example.appptin;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
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

import org.json.JSONException;
import org.json.JSONObject;

public class QrFragment extends Fragment {

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        TextView txtResultant = rootView.findViewById(R.id.txtResultant);
        Button btnScan = rootView.findViewById(R.id.btnScan);
        Button button_recepta = rootView.findViewById(R.id.button_recepta);
        button_recepta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obrir pop-up
                popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
            }
        });

        initPopup();

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
        Resources r = getResources();
        String apiUrl = r.getString(R.string.api_base_url);

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
                        }
                    });
                    queue.add(jsonObjectRequest);
                } else if (contents.charAt(0) == '0') {
                    String recepta = contents.substring(1);
                    showPopupDialog("Aquest QR correspon a una recepta", "Encara estem treballant per acabar de implementar-ho");
                    // Resta de lògica per a la recepta
                } else {
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
                String text = editText.getText().toString();
                // Mostrar el text en la pantalla home
                showPopupDialog("Text introduït", text);
                popupWindow.dismiss();
            }
        });
    }

    private void showPopupDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }
}
