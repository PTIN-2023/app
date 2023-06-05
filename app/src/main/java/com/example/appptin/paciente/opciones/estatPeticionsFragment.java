package com.example.appptin.paciente.opciones;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.appptin.Medicament;
import com.example.appptin.MedicamentAdapter;
import com.example.appptin.Peticio;
import com.example.appptin.R;
import com.example.appptin.login;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link estatPeticionsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class estatPeticionsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerPeticions;

    public estatPeticionsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment estatPaquetsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static estatPeticionsFragment newInstance(String param1, String param2) {
        estatPeticionsFragment fragment = new estatPeticionsFragment();
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
        View view = inflater.inflate(R.layout.fragment_estat_paquets, container, false);
        recyclerPeticions = view.findViewById(R.id.peticions_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerPeticions.setLayoutManager(layoutManager);

        ArrayList<Peticio> list_peticions = new ArrayList<>();
        PeticioAdapter adapter = new PeticioAdapter(list_peticions,getActivity());
        recyclerPeticions.setAdapter(adapter);

        Peticio peticioProva = new Peticio(1, "a@a", "a", "a", "a", "a", false, new ArrayList<>());
        list_peticions.add(peticioProva);

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        Resources r = getResources();
        String apiUrl = r.getString(R.string.api_base_url);
        String url = apiUrl + "/api/list_patient_orders"; // Reemplaça amb l'adreça completa de l'API per obtenir els medicaments disponibles
        JSONObject jsonObject = new JSONObject();

        try {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserPref", Context.MODE_PRIVATE);
            String session_token = sharedPreferences.getString("session_token", "Valor nulo");
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

        JsonArrayRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener() {
            public void onResponse(JSONArray response) {
                System.out.println("###########################################################3");
                System.out.println("MENSAJE: " + response);
                /*try {



                    JSONObject jsonObject = response.getJSONObject();

                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonObject = response.getJSONObject(i);

                        // Acceder a los campos del objeto JSON
                        //Variables iguals a variables del constructor de Peticio
                        double ID = jsonObject.getDouble("medicine_identifier");
                        String medicine_name = jsonObject.getString("medicine_name");
                        String typeOfAdministration = jsonObject.getString("type_of_administration");
                        String form = jsonObject.getString("form");
                        String data = jsonObject.getString("date");
                        String state = jsonObject.getString("state");
                        boolean prescriptionNeeded = jsonObject.getBoolean("prescription_needed");

                        JSONArray jsonarray_prospecto = jsonObject.getJSONArray("excipient");
                        ArrayList<String> excipients = new ArrayList<String>();
                        for (int j = 0; j < jsonarray_prospecto.length(); j++) {
                            excipients.add(jsonarray_prospecto.getString(j));
                        }


                        list_peticions.add(new Peticio(ID,medicine_name,typeOfAdministration,form,data,state, prescriptionNeeded, excipients));

                    }
                    afegirLayoutPeticio(list_peticions);
                } catch (JSONException e) {
                    e.printStackTrace();
                }*/
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Manejo de errores de la solicitud
                error.printStackTrace();
            }
        });

        queue.add(jsonArrayRequest);
        return view;
    }

    private void afegirLayoutPeticio(ArrayList<Peticio> peticions) {
        //Adapter del medicament recyclerView
        PeticioAdapter adapter = new PeticioAdapter(peticions,getActivity());
        recyclerPeticions.setAdapter(adapter);

        for (Peticio peticio : peticions) {
            // Crear el LinearLayout para cada peticio
            LinearLayout peticioLayout = new LinearLayout(getActivity());
            peticioLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            peticioLayout.setOrientation(LinearLayout.VERTICAL);

            // Configurar el ID de la peticio
            TextView nombreTextView = new TextView(getActivity());
            nombreTextView.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));

            // Obtindre la referència al TextView del XML desitjat
            // Pots utilitzar la següent línia de codi per obtenir la referència al TextView del XML:
             TextView textView = (TextView) peticioLayout.findViewById(R.id.ID);
            // On "nomTextView" és l'identificador del TextView en el XML

            textView.setText("Nou text per a ID");

            // Agregar el ImageView y el TextView al LinearLayout del medicamento
            //peticioLayout.addView(imageView);
            //peticioLayout.addView(nombreTextView);

            // Agregar el LinearLayout del medicamento al contenedor principal
            recyclerPeticions.addView(peticioLayout);

            // Agregar el OnClickListener al LinearLayout del medicamento seleccionado
            /*peticioLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    abrirMedicamentoSeleccionado(medicament);
                }
            });*/
        }
    }
}