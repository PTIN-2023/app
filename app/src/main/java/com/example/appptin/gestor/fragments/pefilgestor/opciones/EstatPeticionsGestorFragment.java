package com.example.appptin.gestor.fragments.pefilgestor.opciones;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.appptin.Peticio;
import com.example.appptin.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EstatPeticionsGestorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EstatPeticionsGestorFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerPeticions;

    public EstatPeticionsGestorFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EstatPeticionsGestorFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EstatPeticionsGestorFragment newInstance(String param1, String param2) {
        EstatPeticionsGestorFragment fragment = new EstatPeticionsGestorFragment();
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

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_estat_peticions_gestor, container, false);
        recyclerPeticions = view.findViewById(R.id.peticions_recycler_g);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerPeticions.setLayoutManager(layoutManager);


        ArrayList<Peticio> list_peticions = new ArrayList<>();
        PeticioGestorAdapter adapter = new PeticioGestorAdapter(list_peticions, getActivity());
        recyclerPeticions.setAdapter(adapter);

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        Resources r = getResources();
        String apiUrl = r.getString(R.string.api_base_url);
        String url = apiUrl + "/api/list_all_orders"; // Reemplaça amb l'adreça completa de l'API per obtenir els medicaments disponibles
        JSONObject jsonObject = new JSONObject();


        try {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserPref", Context.MODE_PRIVATE);
            String session_token = sharedPreferences.getString("session_token", "Valor nulo");
            //System.out.println("Token logout: " + sharedPreferences.getString("session_token", "No value"));
            //jsonObject.put("session_token", sharedPreferences.getString("session_token", "No value"));
            //et_email= sharedPreferences.getString("user_email", "Valor nulo");;
            System.out.println(session_token);

            //JSONObject jsonObject = new JSONObject();
            jsonObject.put("session_token", session_token);
            Integer orders_per_page = 2;
            jsonObject.put("orders_per_page", orders_per_page);
            Integer page = 1;
            jsonObject.put("page", page);
            //jsonBody.put(jsonObject);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {

                    public void onResponse(JSONObject response) {
                        System.out.println("MENSAJE: " + response);

                        try {

                            String result = response.getString("result");
                            if (result.equals("ok")) {
                                JSONArray data = response.getJSONArray("orders");
                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject order = data.getJSONObject(i);
                                    int orderIdentifier = order.getInt("order_identifier");
                                    String date = order.getString("date");
                                    String state = order.getString("state");
                                    //String email_pacient = order.getString("patient_email");
                                    //JSONArray medicineData = null;
                                    //JSONArray medicineList = order.getJSONArray("medicine_list");
                                    //JSONArray medicines = new JSONArray<>();
                                    JSONArray medicineList = order.getJSONArray("medicine_list");
                                    ArrayList<JSONArray> medicines = new ArrayList<>();
                                    System.out.println(orderIdentifier);

                                    for (int j = 0; j < medicineList.length(); j++) {
                                        JSONArray medicineData = medicineList.getJSONArray(j);
                                        System.out.println("medicina" + medicineData.getJSONObject(0));
                                        System.out.println("cantitat" + medicineData.getInt(1));

                                        medicines.add(medicineList.getJSONArray(j));

                                        // Aquí pots realitzar les operacions addicionals que necessitis amb el national code i la quantitat
                                    }

                                    System.out.println("Order Identifier: " + orderIdentifier);
                                    System.out.println("Date: " + date);
                                    System.out.println("State: " + state);
                                    // Imprimir los detalles de los medicamentos
                                    /*for (Medicine medicine : medicines) {
                                        System.out.println("Medicine: " + medicine.getName());
                                        // Imprimir otros detalles del medicamento
                                    }*/
                                    String et_email = null;
                                    list_peticions.add(new Peticio(et_email, orderIdentifier, date, state, order.getJSONArray("medicine_list")));
                                }
                                Creacio_elements_RecyclerView(list_peticions);
                            }


                            //afegirLayoutPeticio(response);
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

        queue.add(jsonObjectRequest);
        return view;
    }

    private void Creacio_elements_RecyclerView(ArrayList<Peticio> list_peticions ){
        //Creación de LayoutManager que se encarga de la disposición de los elementos del RecyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerPeticions.setLayoutManager(layoutManager);

        // Creación del adapter con la nueva lista de elementos búscados
        PeticioGestorAdapter adapter = new PeticioGestorAdapter(list_peticions,getActivity());

        recyclerPeticions.setAdapter(adapter);

    }

}