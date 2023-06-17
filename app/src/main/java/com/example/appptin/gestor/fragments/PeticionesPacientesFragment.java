package com.example.appptin.gestor.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.appptin.Medicament;
import com.example.appptin.MedicamentAdapter;
import com.example.appptin.Peticio;
import com.example.appptin.R;
import com.example.appptin.login;
import com.example.appptin.paciente.opciones.PeticioAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PeticionesPacientesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PeticionesPacientesFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerPeticions;

    public PeticionesPacientesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PeticionesPacientesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PeticionesPacientesFragment newInstance(String param1, String param2) {
        PeticionesPacientesFragment fragment = new PeticionesPacientesFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_peticiones_pacientes, container, false);
        recyclerPeticions = view.findViewById(R.id.peticions_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerPeticions.setLayoutManager(layoutManager);

        ArrayList<Peticio> list_peticions = new ArrayList<>();
        PeticioAdapter adapter = new PeticioAdapter(list_peticions,getActivity());
        recyclerPeticions.setAdapter(adapter);

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        Resources r = getResources();
        String apiUrl = r.getString(R.string.api_base_url);
        String checkTokenUrl = apiUrl + "/api/checktoken";
        String listOrdersUrl = apiUrl + "/api/list_patient_orders";
        JSONObject jsonObject = new JSONObject();
        final String session_token;

        try {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserPref", Context.MODE_PRIVATE);
            //String session_token = sharedPreferences.getString("session_token", "Valor nulo");
            //String user_role = sharedPreferences.getString("user_role", "Valor nulo");
            session_token= login.getSession_token();
            System.out.println(session_token);

            jsonObject.put("token", session_token);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest tokenRequest = new JsonObjectRequest
                (Request.Method.POST, checkTokenUrl, jsonObject, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("MENSAJE: " + response);
                        try {
                            String valid = response.getString("valid");
                            String type = response.getString("type");

                            if (valid.equals("ok") && type.equals("manager")) {
                                jsonObject.put("session_token", login.getSession_token());
                                JsonObjectRequest allOrdersRequest = new JsonObjectRequest
                                        (Request.Method.POST, listOrdersUrl, jsonObject, new Response.Listener<JSONObject>() {

                                            @Override
                                            public void onResponse(JSONObject response) {
                                                System.out.println("MENSAJE: " + response);
                                                try {
                                                    String result = response.getString("result");
                                                    if (result.equals("success")) {
                                                        JSONArray data = response.getJSONArray("data");
                                                        for (int i = 0; i < data.length(); i++) {
                                                            JSONObject order = data.getJSONObject(i);
                                                            int orderIdentifier = order.getInt("order_identifier");
                                                            String date = order.getString("date");
                                                            String state = order.getString("state");

                                                            JSONArray medicineList = order.getJSONArray("medicine_list");
                                                            ArrayList<JSONObject> medicines = new ArrayList<>();
                                                            for (int j = 0; j < medicineList.length(); j++) {
                                                                medicines.add(medicineList.getJSONObject(j));
                                                                System.out.println("Medicine: " + medicines.get(j));
                                                                //JSONObject medicine = medicineList.getJSONObject(j);
                                                            }

                                                            System.out.println("Order Identifier: " + orderIdentifier);
                                                            System.out.println("Date: " + date);
                                                            System.out.println("State: " + state);
                                                            // Imprimir los detalles de los medicamentos
                                                        /*for (Medicine medicine : medicines) {
                                                            System.out.println("Medicine: " + medicine.getName());
                                                            // Imprimir otros detalles del medicamento
                                                        }*/
                                                            list_peticions.add(new Peticio(orderIdentifier, date, state, medicines));
                                                        }
                                                        Creacio_elements_RecyclerView(list_peticions);
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }, new Response.ErrorListener() {

                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                // Manejo de errores de la solicitud
                                                String responseBody;
                                                try {
                                                    responseBody = new String(error.networkResponse.data, "utf-8");
                                                    Log.d("Error.Response", responseBody);
                                                } catch (UnsupportedEncodingException e) {
                                                    //Handle a decode error
                                                }
                                            }
                                        });

                                queue.add(allOrdersRequest);
                            } else {
                                Toast.makeText(getActivity(), "No tienes permisos para ver todas las peticiones", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Manejo de errores de la solicitud
                        error.printStackTrace();
                    }
                });
        queue.add(tokenRequest);
        return view;
    }

    private void Creacio_elements_RecyclerView(ArrayList<Peticio> list_peticions ){
        //Creación de LayoutManager que se encarga de la disposición de los elementos del RecyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerPeticions.setLayoutManager(layoutManager);

        // Creación del adapter con la nueva lista de elementos búscados
        PeticioAdapter adapter = new PeticioAdapter(list_peticions,getActivity());

        recyclerPeticions.setAdapter(adapter);

    }
}