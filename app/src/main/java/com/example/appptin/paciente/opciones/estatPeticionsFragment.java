package com.example.appptin.paciente.opciones;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
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
    private ImageView iv_regresar_peticio;
    private String et_email;

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

        iv_regresar_peticio = view.findViewById(R.id.iv_peticions_back);
        iv_regresar_peticio.setOnClickListener(regresar);

        //Peticio peticioProva = new Peticio(1, "a@a", "a", new ArrayList<>());
        //list_peticions.add(peticioProva);

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        Resources r = getResources();
        String apiUrl = r.getString(R.string.api_base_url);
        String url = apiUrl + "/api/list_patient_orders"; // Reemplaça amb l'adreça completa de l'API per obtenir els medicaments disponibles
        JSONObject jsonObject = new JSONObject();

        try {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserPref", Context.MODE_PRIVATE);
            String session_token = sharedPreferences.getString("session_token", "Valor nulo");
            et_email= sharedPreferences.getString("user_email", "Valor nulo");;
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

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("MENSAJE: " + response);

                        try {
                            //JSONObject jsonObject = new JSONObject();
                            //jsonObject = response;
                            //System.out.println("result: " + response.getString("result"));

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
                                    list_peticions.add(new Peticio(et_email, orderIdentifier, date, state, medicines));
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
        PeticioAdapter adapter = new PeticioAdapter(list_peticions,getActivity());

        recyclerPeticions.setAdapter(adapter);

    }

    private View.OnClickListener regresar = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager(); // Si estás en un Fragment, utiliza getFragmentManager()
            if (fragmentManager.getBackStackEntryCount() > 0) {
                // Retrocede en la pila de fragmentos
                fragmentManager.popBackStack();
            }

        }
    };

    private void afegirLayoutPeticio(JSONObject peticions) {
       ////Adapter del medicament recyclerView
       //PeticioAdapter adapter = new PeticioAdapter(peticions,getActivity());
       //recyclerPeticions.setAdapter(adapter);

       //for (Peticio peticio : peticions) {
       //    // Crear el LinearLayout para cada peticio
       //    LinearLayout peticioLayout = new LinearLayout(getActivity());
       //    peticioLayout.setLayoutParams(new LinearLayout.LayoutParams(
       //            LinearLayout.LayoutParams.MATCH_PARENT,
       //            LinearLayout.LayoutParams.WRAP_CONTENT));
       //    peticioLayout.setOrientation(LinearLayout.VERTICAL);

       //    // Configurar el ID de la peticio
       //    TextView nombreTextView = new TextView(getActivity());
       //    nombreTextView.setLayoutParams(new LinearLayout.LayoutParams(
       //            LinearLayout.LayoutParams.WRAP_CONTENT,
       //            LinearLayout.LayoutParams.WRAP_CONTENT
       //    ));

            // Obtindre la referència al TextView del XML desitjat
            // Pots utilitzar la següent línia de codi per obtenir la referència al TextView del XML:
             //TextView textView = (TextView) peticioLayout.findViewById(R.id.ID);
            // On "nomTextView" és l'identificador del TextView en el XML

            // Agregar el ImageView y el TextView al LinearLayout del medicamento
            //peticioLayout.addView(imageView);
            //peticioLayout.addView(nombreTextView);

            // Agregar el LinearLayout del medicamento al contenedor principal
            //recyclerPeticions.addView(peticioLayout);

            // Agregar el OnClickListener al LinearLayout del medicamento seleccionado
            /*peticioLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    abrirMedicamentoSeleccionado(medicament);
                }
            });*/

    }
}